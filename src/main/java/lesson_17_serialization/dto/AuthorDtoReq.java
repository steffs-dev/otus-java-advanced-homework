package lesson_17_serialization.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO-класс (запрос) для поиска книг по автору.
 *
 * <p>Используется для десериализации входящего JSON-запроса.</p>
 *
 * @param firstName   имя автора
 * @param lastName    фамилия автора; в JSON ожидается поле "author_surname"
 * @param middleName  отчество автора; может отсутствовать
 * @param requestName имя запроса; не сериализуется, добавляется при обработке файла
 */

public record AuthorDtoReq(String firstName,
                           @JsonProperty("author_surname")
                           String lastName,
                           @JsonInclude(JsonInclude.Include.NON_NULL)
                           String middleName,
                           @JsonIgnore
                           String requestName
) {
}
