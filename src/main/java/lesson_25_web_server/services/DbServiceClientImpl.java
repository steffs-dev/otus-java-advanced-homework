package lesson_25_web_server.services;

import lesson_23_cache.cachehw.HwCache;
import lesson_23_cache.cachehw.MyCache;
import lesson_23_cache.core.repository.DataTemplate;
import lesson_23_cache.core.sessionmanager.TransactionManager;
import lesson_25_web_server.dto.CredentialsDtoReq;
import lesson_25_web_server.model.Client;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DbServiceClientImpl implements DBServiceClient {
    static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    final DataTemplate<Client> clientDataTemplate;
    final TransactionManager transactionManager;
    final HwCache<Long, Client> clientCache;

    public DbServiceClientImpl(TransactionManager transactionManager,
                               DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.clientCache = new MyCache<>();
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, client);
                log.info("client saved in DB: {}", savedClient);
                clientCache.put(savedClient.getId(), savedClient);
                log.info("client saved in cache: {}", savedClient);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, client);
            log.info("client updated in DB: {}", savedClient);
            clientCache.put(savedClient.getId(), savedClient);
            log.info("client updated in cache: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(Long id) {
        Optional<Client> cachedClient = Optional.ofNullable(clientCache.get(id));
        if (cachedClient.isPresent()) {
            log.info("client from cache: {}", cachedClient);
            return cachedClient;
        }

        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client from DB: {}", clientOptional);
            clientOptional.ifPresent(client -> {
                clientCache.put(client.getId(), client);
            });
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientsListFromCache = new ArrayList<>(clientCache.getAll().values());
        if (!clientsListFromCache.isEmpty()) {
            log.info("clientsList from cache: {}", clientsListFromCache);
            return clientsListFromCache;
        }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientListFromDb = clientDataTemplate.findAll(session);
            log.info("clientList from DB:{}", clientListFromDb);
            clientListFromDb.forEach(client -> clientCache.put(client.getId(), client));
            return clientListFromDb;
        });
    }

    @Override
    public Optional<Client> findByLogin(CredentialsDtoReq credentialsDtoReq) {
        String login = credentialsDtoReq.login();
        return transactionManager.doInReadOnlyTransaction(session -> {
            List<Client> clientList = clientDataTemplate.findByEntityField(session, "login", login);
            log.info("clientList from DB:{}", clientList);
            if (clientList.size() != 1) {
                log.warn("login policy break - saved several clients with the same login {}", login);
                return Optional.empty();
            }
            return Optional.of(clientList.getFirst());
        });
    }
}
