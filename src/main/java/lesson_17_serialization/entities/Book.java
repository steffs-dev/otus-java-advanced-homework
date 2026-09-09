package lesson_17_serialization.entities;

import java.util.Objects;

/**
 * Сущность "Книга".
 *
 * <p>Содержит название книги и ссылку на автора.</p>
 */

public class Book {
    /**
     * Название книги.
     */

    private String title;

    /**
     * Автор книги.
     */

    private Author author;

    /**
     * Создает книгу с названием и автором.
     *
     * @param title  название книги
     * @param author автор книги
     */

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public String toString() {
        return "Book{" + "title=" + title + ", author=" + author + '}';
    }
}
