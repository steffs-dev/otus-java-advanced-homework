package lesson_25_web_server.dto;

import lesson_25_web_server.model.Client;
import lombok.Builder;

import java.util.function.Function;

@Builder
public record ClientDtoResp(Long id, String name, String login) {

    public static Function<Client, ClientDtoResp> createFromClient() {
        return c -> ClientDtoResp.builder()
                .id(c.getId())
                .name(c.getName())
                .login(c.getLogin())
                .build();
    }
}
