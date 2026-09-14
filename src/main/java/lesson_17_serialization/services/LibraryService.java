package lesson_17_serialization.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lesson_17_serialization.dto.BooksDtoResp;
import lesson_17_serialization.repositories.LibraryRepository;
import lesson_17_serialization.utils.JsonUtil;
import lesson_17_serialization.dao.LibraryDaoImpl;
import lesson_17_serialization.dto.AuthorDtoReq;
import lesson_17_serialization.entities.Author;
import lesson_17_serialization.entities.Book;

import java.util.List;
import java.util.Map;

/**
 * Сервис библиотеки.
 *
 * <p>Содержит основную бизнес-логику:
 * поиск книг по автору, проверку запросов и сохранение книг.</p>
 */

public class LibraryService {

    /**
     * DAO для работы с данными библиотеки.
     */

    private final LibraryDaoImpl dao;

    /**
     * Создает сервис библиотеки.
     *
     * @param libRepo репозиторий библиотеки
     */

    public LibraryService(LibraryRepository libRepo) {
        this.dao = new LibraryDaoImpl(libRepo);
    }

    /**
     * Выполняет поиск книг по данным автора из DTO.
     *
     * <p>Алгоритм работы:</p>
     * <ol>
     *     <li>Проверяет входной DTO.</li>
     *     <li>Создает сущность {@link Author} из DTO.</li>
     *     <li>Выполняет поиск книг через DAO.</li>
     *     <li>Формирует JSON-ответ.</li>
     * </ol>
     *
     * @param dto входной DTO-запрос
     * @return DTO-ответ с именем запроса и JSON-строкой
     * @throws JsonProcessingException если возникает ошибка сериализации
     */

    public BooksDtoResp findBooksByAuthor(AuthorDtoReq dto) throws JsonProcessingException {
        validate(dto);

        Author author = Author.createFromDto(dto);
        List<Book> books = dao.findBooksByAuthor(author);
        String requestName = dto.requestName();
        String json;
        if (books.isEmpty()) {
            json = JsonUtil.getMAPPER().writeValueAsString(
                    Map.of(
                            "requestName", requestName,
                            "response", "No books found"
                    )
            );
        } else {
            json = JsonUtil.getMAPPER().writeValueAsString(books);
        }
        return new BooksDtoResp(requestName, json);
    }

    /**
     * Сохраняет список книг.
     *
     * @param newBooks список книг для сохранения
     */

    public void saveBooks(List<Book> newBooks) {
        dao.saveBooks(newBooks);
    }

    /**
     * Сохраняет одну книгу.
     *
     * @param newBook книга для сохранения
     */

    public void saveBook(Book newBook) {
        dao.saveBook(newBook);
    }

    /**
     * Проверяет обязательные поля запроса.
     *
     * <p>Имя и фамилия автора являются обязательными.</p>
     *
     * @param dto проверяемый DTO-запрос
     * @throws IllegalArgumentException если обязательное поле отсутствует или пустое
     */

    private void validate(AuthorDtoReq dto) {
        if (dto.firstName() == null || dto.firstName().isBlank()) {
            throw new IllegalArgumentException("firstName is required");
        }

        if (dto.lastName() == null || dto.lastName().isBlank()) {
            throw new IllegalArgumentException("author_surname is required");
        }
    }
}
