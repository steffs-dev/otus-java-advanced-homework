package lesson_17_serialization;

import lesson_17_serialization.entities.Author;
import lesson_17_serialization.entities.Book;
import lesson_17_serialization.entities.Library;
import lesson_17_serialization.ui.Console;
import lesson_17_serialization.ui.UI;
import lesson_17_serialization.utils.ConcurrencyUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

/**
 * Фасад приложения.
 *
 * <p>Инициализирует библиотеку, пользовательский интерфейс,
 * пулы потоков и запускает сессию обработки запросов.</p>
 */

public class LibraryFacade {

    /**
     * Библиотека с данными и сервисом.
     */

    private final Library lib;

    /**
     * Планировщик для отложенных и периодических задач.
     */

    private final ScheduledExecutorService scheduler;

    /**
     * Пул потоков для рабочих задач сессии.
     */

    private final ExecutorService executor;

    /**
     * Интерфейс пользовательского ввода-вывода.
     */

    private final UI ui;

    /**
     * Создает фасад приложения.
     *
     * <p>Также наполняет репозиторий тестовыми данными.</p>
     */

    public LibraryFacade() {
        this.lib = new Library();
        addDefaultRepositoryData();
        this.ui = new Console();
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.executor = Executors.newFixedThreadPool(4);
    }

    /**
     * Запускает обслуживание посетителя.
     *
     * <p>Метод:</p>
     * <ol>
     *     <li>Создает рабочие директории.</li>
     *     <li>Создает {@link SessionManager}.</li>
     *     <li>Запускает сессию.</li>
     *     <li>После завершения освобождает ресурсы.</li>
     * </ol>
     *
     * @param dirToScan директория входящих запросов
     * @param dirToResp директория исходящих ответов
     */

    public void serveVisitor(String dirToScan, String dirToResp) {
        try {
            Files.createDirectories(Path.of(dirToScan));
            Files.createDirectories(Path.of(dirToResp));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SessionManager session = new SessionManager(executor, lib, ui, dirToScan, dirToResp);
        try {
            handleSession(session);
        } finally {
            ConcurrencyUtil.shutdown(scheduler, executor);
            ui.close();
        }
    }

    /**
     * Управляет жизненным циклом сессии.
     *
     * <p>Через 20 секунд сессия автоматически останавливается.</p>
     *
     * @param session управляемая сессия
     */

    private void handleSession(SessionManager session) {
        scheduler.schedule(session::stopRunning, 20, TimeUnit.SECONDS);
        session.execute();
    }

    /**
     * Добавляет дефолтный набор книг и авторов в репозиторий.
     */

    private void addDefaultRepositoryData() {
        Author tolstoy = Author.builder()
                .firstName("Lev")
                .lastName("Tolstoy")
                .middleName("Nikolaevich")
                .build();

        Author chekhov = Author.builder()
                .firstName("Anton")
                .lastName("Chekhov")
                .build();

        lib.getLibService().saveBook(new Book("War and Peace", tolstoy));
        lib.getLibService().saveBook(new Book("Anna Karenina", tolstoy));
        lib.getLibService().saveBook(new Book("The Cherry Orchard", chekhov));
    }
}
