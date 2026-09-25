package lesson_23_cache.crm.service;

import java.util.*;

import lesson_23_cache.cachehw.HwCache;
import lesson_23_cache.cachehw.HwListener;
import lesson_23_cache.cachehw.MyCache;
import lesson_23_cache.core.repository.DataTemplate;
import lesson_23_cache.core.sessionmanager.TransactionManager;
import lesson_23_cache.crm.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private final HwCache<Long, Client> clientCache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate,
                               HwListener<Long, Client> ... listener) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.clientCache = new MyCache<>();
        addCacheListeners(listener);
    }

    private void addCacheListeners(HwListener<Long, Client>[] listener) {
        Arrays.stream(listener).forEach(clientCache::addListener);
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
    public Optional<Client> getClient(long id) {
        Optional<Client> cachedClient = Optional.ofNullable(clientCache.get(id));
        if (cachedClient.isPresent()) {
            log.info("client from cache: {}", cachedClient);
            return cachedClient;
        }

        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client from DB: {}", clientOptional);
            cachedClient.ifPresent(client -> {clientCache.put(client.getId(), client);});
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientsList = new ArrayList<>(clientCache.getAll().values());
        if (!clientsList.isEmpty()) {
            log.info("clientsList from cache: {}", clientsList);
            return clientsList; }
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList from DB:{}", clientList);
            clientsList.forEach(client -> clientCache.put(client.getId(), client));
            return clientList;
        });
    }
}
