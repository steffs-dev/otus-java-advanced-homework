package lesson_17_serialization.dao;

import lesson_17_serialization.entities.Author;
import lesson_17_serialization.entities.Book;
import lesson_17_serialization.repositories.LibraryRepository;

import java.util.List;
import java.util.Objects;

/**
 * Реализация интерфейса {@link LibraryDao}.
 *
 * <p>Класс выполняет операции над данными библиотеки,
 * хранящимися в объекте {@link LibraryRepository}.</p>
 */

public class LibraryDaoImpl implements LibraryDao {

    /**
     * Репозиторий библиотеки, в котором хранятся книги.
     */

    private final LibraryRepository libRepo;

    /**
     * Создает DAO с указанным репозиторием.
     *
     * @param libRepo репозиторий библиотеки
     */

    public LibraryDaoImpl(LibraryRepository libRepo) {
        this.libRepo = libRepo;
    }


    /**
     * {@inheritDoc}
     *
     * <p>Поиск выполняется по всем книгам репозитория.
     * Сравнение авторов выполняется через {@link Objects#equals(Object, Object)},
     * чтобы корректно обрабатывать {@code null}.</p>
     */

    @Override
    public List<Book> findBooksByAuthor(Author author) {
        return libRepo.getBooks().stream()
                .filter(book -> Objects.equals(book.getAuthor(), author))
                .toList();
    }


    /**
     * {@inheritDoc}
     *
     * <p>Сохранение выполняется через метод репозитория,
     * чтобы не изменять неизменяемую копию списка книг.</p>
     */

    @Override
    public void saveBooks(List<Book> newBooks) {
        libRepo.addBooks(newBooks);

    }

    /**
     * {@inheritDoc}
     *
     * <p>Сохранение одной книги выполняется через метод репозитория.</p>
     */

    @Override
    public void saveBook(Book newBook) {
        libRepo.addBook(newBook);
    }
}
