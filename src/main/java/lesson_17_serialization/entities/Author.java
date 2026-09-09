package lesson_17_serialization.entities;

import lesson_17_serialization.dto.AuthorDtoReq;

import java.util.Objects;
import java.util.function.Function;

/**
 * Сущность "Автор".
 *
 * <p>Объекты создаются через паттерн "строитель" {@link Builder},
 * чтобы упростить создание объектов с необязательными полями.</p>
 */

public class Author {

    /**
     * Имя автора.
     */

    private final String firstName;

    /**
     * Фамилия автора.
     */

    private final String lastName;

    /**
     * Отчество автора.
     */

    private final String middleName;

    /**
     * Приватный конструктор для создания объекта через {@link Builder}.
     *
     * @param builder строитель, содержащий значения полей
     */

    private Author(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.middleName = builder.middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                '}';
    }

    /**
     * Создает новый экземпляр строителя.
     *
     * @return новый {@link Builder}
     */

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Вложенный класс-строитель для создания объектов {@link Author}.
     */

    public static class Builder {
        private String firstName;
        private String lastName;
        private String middleName;

        /**
         * Устанавливает имя автора.
         *
         * @param firstName имя автора
         * @return текущий строитель
         */

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Устанавливает фамилию автора.
         *
         * @param lastName фамилия автора
         * @return текущий строитель
         */

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Устанавливает отчество автора.
         *
         * @param middleName отчество автора
         * @return текущий строитель
         */

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        /**
         * Создает объект {@link Author} на ранее заданных полей.
         *
         * @return новый объект автора
         */

        public Author build() {
            return new Author(this);
        }
    }

    /**
     * Создает объект {@link Author} из DTO-запроса.
     *
     * <p>Внутри используется функциональный интерфейс {@link Function},
     * чтобы показать возможность преобразования DTO в сущность через функцию.</p>
     *
     * @param dto DTO-запрос с данными автора
     * @return объект автора
     */

    public static Author createFromDto(AuthorDtoReq dto) {
        Function<AuthorDtoReq, Author> authorFunction =
                d -> Author.builder()
                        .firstName(d.firstName())
                        .lastName(d.lastName())
                        .middleName(d.middleName())
                        .build();
        return authorFunction.apply(dto);
    }

    /**
     * Сравнивает текущего автора с другим объектом.
     *
     * <p>Авторы считаются равными, если равны их имя, фамилия и отчество.</p>
     *
     * @param o объект для сравнения
     * @return {@code true}, если объекты равны
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName)
                && Objects.equals(middleName, author.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, middleName);
    }
}
