package lesson_17_serialization.dto;

/**
 * DTO-класс (ответ), содержащий имя запроса и результирующую строку в формате JSON.
 *
 * @param requestName имя обработанного запроса
 * @param response    строка ответа в формате JSON
 */

public record BooksDtoResp(
        String requestName,
        String response
) {
}
