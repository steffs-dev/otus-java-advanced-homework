package lesson_25_web_server.repository;

import lesson_23_cache.core.repository.DataTemplateHibernate;
import lesson_23_cache.core.repository.HibernateUtils;
import lesson_23_cache.core.sessionmanager.TransactionManagerHibernate;
import lesson_25_web_server.model.Client;
import lesson_25_web_server.services.DBServiceClient;
import lesson_25_web_server.services.DbServiceClientImpl;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBInit {
    private static final Logger log = LoggerFactory.getLogger(DBInit.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private DBServiceClient dbServiceClient;

    public DBServiceClient getDbServiceClient() {
        return dbServiceClient;
    }

    public DBInit() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        addRecords();
        log.info("-------------------------");
    }

    private void addRecords() {
        if (!dbServiceClient.findAll().isEmpty()) {
            log.info("DB has already been filled with records");
            return;
        }
        dbServiceClient.saveClient(new Client(null, "first", "user1", "111"));
        dbServiceClient.saveClient(new Client(null, "second", "user2", "111"));

        var clientSecond = dbServiceClient.saveClient(new Client(null, "third", "user3", "111"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}
