package lesson_25_web_server.services;

import lesson_25_web_server.dto.CredentialsDtoReq;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceClient dbServiceClient;

    public UserAuthServiceImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public boolean authenticate(CredentialsDtoReq credentialsDtoReq) {
        return dbServiceClient.findByLogin(credentialsDtoReq)
                .map(client -> client.getPassword().equals(credentialsDtoReq.password()))
                .orElse(false);
    }
}
