package lesson_17_serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import lesson_17_serialization.dto.AuthorDtoReq;
import lesson_17_serialization.dto.BooksDtoResp;
import lesson_17_serialization.entities.Library;
import lesson_17_serialization.services.FilesService;
import lesson_17_serialization.ui.UI;
import lesson_17_serialization.utils.ConcurrencyUtil;
import lesson_17_serialization.utils.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Менеджер сессии обработки запросов.
 *
 * <p>Отвечает за:</p>
 * <ul>
 *     <li>периодическое сканирование директории с запросами;</li>
 *     <li>чтение и разбор входящих JSON-файлов;</li>
 *     <li>передачу запросов в очередь обработки;</li>
 *     <li>получение ответов из очереди;</li>
 *     <li>запись ответов в файлы;</li>
 *     <li>обработку пользовательского ввода;</li>
 *     <li>остановку сессии.</li>
 * </ul>
 */

public class SessionManager {

    /**
     * Библиотека с бизнес-логикой.
     */

    private final Library library;

    /**
     * Сервис работы с файлами запросов и ответов.
     */

    private final FilesService filesService;

    /**
     * Интерфейс пользовательского ввода-вывода.
     */

    private final UI ui;

    /**
     * Очередь входящих запросов.
     */

    private final BlockingQueue<AuthorDtoReq> requestQueue = new LinkedBlockingQueue<>();

    /**
     * Очередь подготовленных ответов.
     */

    private final BlockingQueue<BooksDtoResp> responseQueue = new LinkedBlockingQueue<>();

    /**
     * Пул потоков для выполнения рабочих задач.
     */

    private final ExecutorService executor;

    /**
     * Планировщик для периодического сканирования файлов.
     */

    private final ScheduledExecutorService scheduler;

    /**
     * Флаг активности сессии.
     */

    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Логгер приложения.
     */

    private static final Logger log = LogManager.getLogger(SessionManager.class);

    /**
     * Задержка, которая позволяет методу {@link #execute()} ждать завершения сессии.
     */

    private final CountDownLatch stopLatch = new CountDownLatch(1);

    /**
     * Создает менеджер сессии.
     *
     * @param executor  пул потоков для рабочих задач
     * @param library   библиотека
     * @param ui        интерфейс ввода-вывода
     * @param dirToScan директория входящих запросов
     * @param dirToResp директория исходящих ответов
     */

    public SessionManager(ExecutorService executor, Library library, UI ui, String dirToScan, String dirToResp) {
        this.library = library;
        this.filesService = new FilesService(dirToScan, dirToResp);
        this.ui = ui;
        this.executor = executor;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Запускает сессию обработки запросов.
     *
     * <p>Метод запускает:</p>
     * <ul>
     *     <li>периодическое сканирование директории;</li>
     *     <li>обработку очереди запросов;</li>
     *     <li>обработку очереди ответов;</li>
     *     <li>обработку пользовательского ввода.</li>
     * </ul>
     */

    public void execute() {
        scheduler.scheduleWithFixedDelay(this::handleRequest, 0, 5, TimeUnit.SECONDS);
        executor.submit(this::processRequests);
        executor.submit(this::processResponses);
        executor.submit(this::handleUI);

        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            close();
        }
    }

    /**
     * Обрабатывает очередь запросов.
     *
     * <p>Достает запрос из очереди, выполняет поиск книг
     * и кладет результат в очередь ответов для дальнейшей обработки.</p>
     */

    private void processRequests() {
        while (running.get()) {
            try {
                AuthorDtoReq req = requestQueue.poll(200, TimeUnit.MILLISECONDS);

                if (req == null) {
                    continue;
                }

                BooksDtoResp resp = createBooksDtoResp(req);

                if (resp != null) {
                    responseQueue.offer(resp);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.warn("Error while processing request: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Обрабатывает очередь ответов.
     *
     * <p>Достает ответ из очереди и записывает его в файл.</p>
     */

    private void processResponses() {
        while (running.get()) {
            try {
                BooksDtoResp resp = responseQueue.poll(200, TimeUnit.MILLISECONDS);

                if (resp == null) {
                    continue;
                }
                filesService.write(resp);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (IOException e) {
                log.warn("Error while writing response {}", e.getMessage());
            } catch (Exception e) {
                log.warn("Error while processing request: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Обрабатывает пользовательский ввод.
     *
     * <p>Команда {@code exit} завершает сессию.</p>
     */

    private void handleUI() {
        while (running.get()) {
            String input;
            try {
                input = ui.readLine();
            } catch (RuntimeException e) {
                log.warn("UI reading error: {}", e.getMessage());
                break;
            }

            if (input == null) {
                break;
            }
            if ("exit".equalsIgnoreCase(input.trim())) {
                stopRunning();
                break;
            }
        }
    }

    /**
     * Останавливает сессию.
     *
     * <p>Выставляет флаг остановки и освобождает {@link #stopLatch}.</p>
     */

    public void stopRunning() {
        running.set(false);
        stopLatch.countDown();
    }

    /**
     * Создает ответ по запросу.
     *
     * <p>Если во время обработки возникает ошибка,
     * формируется ответ с текстом ошибки.</p>
     *
     * @param authorDtoReq запрос пользователя
     * @return DTO-ответ
     */

    private BooksDtoResp createBooksDtoResp(AuthorDtoReq authorDtoReq) {
        String requestName = authorDtoReq == null || authorDtoReq.requestName() == null
                ? "unknown_request"
                : authorDtoReq.requestName();

        try {
            return library.getLibService().findBooksByAuthor(authorDtoReq);
        } catch (JsonProcessingException e) {
            log.warn("JSON processing error for request {}: {}", requestName, e.getMessage());
            return createEmptyBooksDtoResp(requestName, "Processing error");
        } catch (IllegalArgumentException e) {
            log.warn("Validation error for request {}: {}", requestName, e.getMessage());
            return createEmptyBooksDtoResp(requestName, "Request validation error");
        } catch (Exception e) {
            log.error("Unexpected error for request {}: {}", requestName, e.getMessage());
            return createEmptyBooksDtoResp(requestName, "Unexpected error");
        }
    }

    /**
     * Создает пустой или ошибочный ответ.
     *
     * <p>Используется, если запрос не может быть корректно обработан.</p>
     *
     * @param requestName имя запроса
     * @param message     сообщение для ответа
     * @return DTO-ответ с сообщением об ошибке или пустым результатом
     */

    private BooksDtoResp createEmptyBooksDtoResp(String requestName, String message) {
        try {
            String json = JsonUtil.getMAPPER().writeValueAsString(
                    Map.of(
                            "requestName", requestName,
                            "response", message
                    )
            );

            return new BooksDtoResp(requestName, json);
        } catch (JsonProcessingException e) {
            String fallbackJson = """
                    {
                      "requestName": "unknown_request",
                      "response": "ERROR"
                    }
                    """;

            return new BooksDtoResp("unknown_request", fallbackJson);
        }
    }

    /**
     * Добавляет ответ в очередь ответов.
     *
     * @param dto ответ для добавления
     */

    private void updatedResponse(BooksDtoResp dto) {
        if (dto != null) {
            responseQueue.offer(dto);
        }
    }

    /**
     * Обрабатывает найденные файлы запросов.
     *
     * <p>Для каждого файла:</p>
     * <ol>
     *     <li>Определяет имя запроса по имени файла.</li>
     *     <li>Читает содержимое файла.</li>
     *     <li>Пытается распознать JSON.</li>
     *     <li>Добавляет корректный запрос в очередь.</li>
     *     <li>Для ошибочных файлов формирует ответ с ошибкой.</li>
     * </ol>
     *
     * @param requests множество новых файлов запросов
     */

    private void updateRequestsCollections(Set<Path> requests) {
        Set<Path> handledFiles = new HashSet<>();
        BlockingQueue<AuthorDtoReq> update = new LinkedBlockingQueue<>();
        for (Path path : requests) {
            String requestName = parseRequestNameFromPath(path);
            try {
                String content = filesService.read(path);

                if (content.isBlank()) {
                    log.warn("Request file is empty: {}", path.getFileName());
                    updatedResponse(createEmptyBooksDtoResp(requestName, "Request file is empty"));
                    handledFiles.add(path);
                    continue;
                }

                AuthorDtoReq interimDtoReq = JsonUtil.getMAPPER().readValue(content, AuthorDtoReq.class);
                AuthorDtoReq dtoReq = enrichWithRequestName(interimDtoReq, path);
                update.offer(dtoReq);
                handledFiles.add(path);
            } catch (JsonProcessingException e) {
                log.warn("Invalid JSON in file {}: {}", path.getFileName(), e.getOriginalMessage());
                updatedResponse(createEmptyBooksDtoResp(requestName, "JSON cannot be parsed"));
                handledFiles.add(path);

            } catch (IOException e) {
                log.warn("Error while reading the file {}: {}",
                        path.getFileName(),
                        e.getMessage());
                updatedResponse(createEmptyBooksDtoResp(requestName, "File cannot be read"));
                handledFiles.add(path);
            }
        }
        updatedReqStack(requestQueue, update);
        filesService.addExecutedFiles(handledFiles);
    }

    /**
     * Извлекает имя запроса из имени файла.
     *
     * <p>Расширение {@code .json} удаляется без учета регистра.</p>
     *
     * @param path путь к файлу запроса
     * @return имя запроса
     */

    private String parseRequestNameFromPath(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.replaceFirst("(?i)\\.json$", "");
    }

    /**
     * Добавляет в DTO имя запроса на основе имени файла.
     *
     * @param dto  исходный DTO-запрос
     * @param path файл запроса
     * @return DTO с заполненным служебным полем {@code requestName}
     */

    private AuthorDtoReq enrichWithRequestName(AuthorDtoReq dto, Path path) {
        return new AuthorDtoReq(
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                parseRequestNameFromPath(path)
        );
    }

    /**
     * Обрабатывает один цикл сканирования директории.
     *
     * <p>Если найдены новые файлы, они передаются в обработку.</p>
     */

    private void handleRequest() {
        Set<Path> requests = scanDirectory();
        if (!requests.isEmpty()) {
            updateRequestsCollections(requests);
        }
    }

    /**
     * Добавляет новые запросы в основную очередь запросов.
     *
     * @param original основная очередь
     * @param update   очередь с новыми запросами
     */

    private void updatedReqStack(BlockingQueue<AuthorDtoReq> original, BlockingQueue<AuthorDtoReq> update) {
        if (!update.isEmpty()) {
            original.addAll(update);
        }
    }

    /**
     * Сканирует директорию с запросами.
     *
     * @return множество найденных новых файлов
     */

    private Set<Path> scanDirectory() {
        Set<Path> requests = new HashSet<>();
        try {
            requests = filesService.findNewFilesToRead();
        } catch (IOException e) {
            log.warn("Error while scanning the directory: {}", e.getMessage());
        }
        return requests;
    }

    /**
     * Закрывает внутренние ресурсы менеджера сессии.
     */

    private void close() {
        ConcurrencyUtil.shutdown(scheduler);
    }
}
