package lesson_17_serialization.dao;

import lesson_17_serialization.entities.Author;
import lesson_17_serialization.entities.Book;

import java.util.List;

/**
 * Интерфейс уровня DAO для работы с книгами библиотеки.
 *
 * <p>Отделяет логику доступа к данным от бизнес-логики и выше стоящих слоев.</p>
 */

public interface LibraryDao {

    /**
     * Возвращает список книг, принадлежащих указанному автору.
     *
     * @param author автор, книги которого нужно найти
     * @return список книг автора
     */

    List<Book> findBooksByAuthor(Author author);

    /**
     * Сохраняет список книг в хранилище.
     *
     * @param newBooks список книг для сохранения
     */

    void saveBooks(List<Book> newBooks);

    /**
     * Сохраняет одну книгу в хранилище.
     *
     * @param newBook книга для сохранения
     */

    void saveBook(Book newBook);
}
