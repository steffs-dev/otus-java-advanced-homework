package lesson_25_web_server.dto;

import lombok.Builder;

@Builder
public record CredentialsDtoReq(String login, String password) {
}
