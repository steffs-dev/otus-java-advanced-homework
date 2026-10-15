package lesson_25_web_server.dto;

import lombok.Builder;

@Builder
public record ClientDtoReq(String firstName, String login, String password) {
}
