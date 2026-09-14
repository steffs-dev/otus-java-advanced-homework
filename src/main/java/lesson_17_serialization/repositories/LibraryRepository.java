package lesson_17_serialization.repositories;

import lesson_17_serialization.entities.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Репозиторий для хранения книг.
 *
 * <p>Внутри используется {@link CopyOnWriteArrayList},
 * чтобы обеспечить безопасную работу со списком в многопоточной среде.</p>
 */

public class LibraryRepository {

    /**
     * Потокобезопасный список книг.
     */

    private final List<Book> books = new CopyOnWriteArrayList<>();

    /**
     * Создает пустой репозиторий.
     */

    public LibraryRepository() {
    }

    /**
     * Создает репозиторий и добавляет в него переданные книги.
     *
     * @param books начальный список книг
     */

    public LibraryRepository(List<Book> books) {
        addBooks(books);
    }

    /**
     * Добавляет одну книгу в репозиторий.
     *
     * @param book книга для добавления
     */

    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Добавляет список книг в репозиторий.
     *
     * @param newBooks список книг для добавления
     */

    public void addBooks(List<Book> newBooks) {
        books.addAll(newBooks);
    }

    /**
     * Возвращает неизменяемую копию списка книг.
     *
     * <p>Это защищает внутренний список от изменения извне.</p>
     *
     * @return копия списка книг
     */

    public List<Book> getBooks() {
        return List.copyOf(books);
    }
}
