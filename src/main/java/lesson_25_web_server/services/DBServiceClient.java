package lesson_25_web_server.services;

import lesson_25_web_server.dto.CredentialsDtoReq;
import lesson_25_web_server.model.Client;

import java.util.List;
import java.util.Optional;


public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(Long id);

    List<Client> findAll();

    Optional<Client> findByLogin(CredentialsDtoReq credentialsDtoReq);
}
