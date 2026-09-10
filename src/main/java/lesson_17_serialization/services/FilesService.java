package lesson_17_serialization.services;

import lesson_17_serialization.dto.BooksDtoResp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис для работы с файлами запросов и ответов.
 *
 * <p>Сканирует директорию с входящими JSON-файлами,
 * читает новые запросы и записывает ответы в отдельную директорию.</p>
 */

public class FilesService {

    /**
     * Директория, в которой появляются файлы запросов.
     */

    private final Path REQ_DIR_PATH;

    /**
     * Директория, в которую записываются файлы ответов.
     */

    private final Path RESP_DIR_PATH;

    /**
     * Расширение обрабатываемых файлов.
     */

    private final String JSON_EXTENSION = ".json";

    /**
     * Множество уже обработанных файлов.
     *
     * <p>Используется потокобезопасное множество,
     * так как файлы могут обрабатываться в нескольких потоках.</p>
     */

    private final Set<Path> readFiles = ConcurrentHashMap.newKeySet();

    /**
     * Создает файловый сервис.
     *
     * @param dirToScan директория для сканирования запросов
     * @param dirToResp директория для записи ответов
     */

    public FilesService(String dirToScan, String dirToResp) {
        REQ_DIR_PATH = Paths.get(dirToScan);
        RESP_DIR_PATH = Paths.get(dirToResp);
    }

    /**
     * Ищет новые файлы запросов, которые еще не были обработаны.
     *
     * <p>Обрабатываются только обычные файлы с расширением {@code .json}.</p>
     *
     * @return множество новых файлов
     * @throws IOException если возникает ошибка доступа к файловой системе
     */

    public Set<Path> findNewFilesToRead() throws IOException {
        try (Stream<Path> stream = Files.walk(REQ_DIR_PATH)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName()
                            .toString()
                            .toLowerCase()
                            .endsWith(JSON_EXTENSION))
                    .filter(p -> !readFiles.contains(p))
                    .collect(Collectors.toUnmodifiableSet());

        }
    }

    /**
     * Помечает файлы как обработанные.
     *
     * @param executedFiles множество успешно обработанных файлов
     */

    public void addExecutedFiles(Set<Path> executedFiles) {
        if (!executedFiles.isEmpty()) {
            readFiles.addAll(executedFiles);
        }
    }

    /**
     * Читает текстовое содержимое файла в формате UTF-8.
     *
     * @param filePath путь к файлу
     * @return содержимое файла
     * @throws IOException если возникает ошибка чтения файла
     */

    public String read(Path filePath) throws IOException {
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    /**
     * Записывает ответ в отдельный файл.
     *
     * <p>Если файл с таким именем уже существует,
     * создается новое имя с суффиксом вида _1, _2 и т.д.</p>
     *
     * @param dto DTO-ответ для записи
     * @throws IOException если возникает ошибка записи файла
     */

    public void write(BooksDtoResp dto) throws IOException {
        Path fullPath = validateOrCreateFileName(dto.requestName());
        try (FileChannel channel = FileChannel.open(fullPath, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE)) {
            ByteBuffer buffer = ByteBuffer.wrap(dto.response().getBytes(StandardCharsets.UTF_8));
            int bytesWrote = channel.write(buffer);
            if (bytesWrote <= 0) {
                throw new IOException("Could not write to " + fullPath);
            }
        }
    }

    /**
     * Подбирает уникальное имя файла ответа.
     *
     * <p>Если файл с исходным именем уже существует,
     * к имени добавляется числовой суффикс.</p>
     *
     * @param requestName имя запроса
     * @return уникальный путь к файлу ответа
     */

    private Path validateOrCreateFileName(String requestName) {
        int counter = 1;
        Path filePath = RESP_DIR_PATH.resolve(requestName + JSON_EXTENSION);
        while (Files.exists(filePath)) {
            filePath = RESP_DIR_PATH.resolve(requestName + "_" + counter + JSON_EXTENSION);
            counter++;
        }
        return filePath;
    }
}
