package lesson_25_web_server.services;

import lesson_25_web_server.dto.CredentialsDtoReq;

public interface UserAuthService {
    boolean authenticate(CredentialsDtoReq credentialsDtoReq);
}
