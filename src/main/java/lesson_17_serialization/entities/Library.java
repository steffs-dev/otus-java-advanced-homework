package lesson_17_serialization.entities;

import lesson_17_serialization.repositories.LibraryRepository;
import lesson_17_serialization.services.LibraryService;

import java.util.List;

/**
 * Класс библиотеки.
 *
 * <p>Объединяет репозиторий с книгами и сервис библиотеки.
 * Является точкой доступа к сервисному слоую.</p>
 */

public class Library {

    /**
     * Репозиторий, в котором хранятся книги.
     */

    private final LibraryRepository libRepo;

    /**
     * Сервис библиотеки, содержащий бизнес-логику.
     */

    private final LibraryService libService;

    /**
     * Создает библиотеку с пустым репозиторием.
     */

    public Library() {
        this.libRepo = new LibraryRepository();
        this.libService = new LibraryService(libRepo);
    }

    /**
     * Создает библиотеку и сразу наполняет репозиторий списком книг.
     *
     * @param books начальный список книг
     */

    public Library(List<Book> books) {
        this.libRepo = new LibraryRepository(books);
        this.libService = new LibraryService(libRepo);
    }

    /**
     * Возвращает сервис библиотеки.
     *
     * @return сервис библиотеки
     */

    public LibraryService getLibService() {
        return libService;
    }
}
