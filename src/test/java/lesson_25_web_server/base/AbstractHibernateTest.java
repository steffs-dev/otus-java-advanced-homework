package lesson_25_web_server.base;

import lesson_23_cache.core.repository.DataTemplate;
import lesson_23_cache.core.repository.DataTemplateHibernate;
import lesson_23_cache.core.repository.HibernateUtils;
import lesson_23_cache.core.sessionmanager.TransactionManager;
import lesson_23_cache.core.sessionmanager.TransactionManagerHibernate;
import lesson_25_web_server.model.Client;
import lesson_25_web_server.repository.DBInit;
import lesson_25_web_server.services.DBServiceClient;
import lesson_25_web_server.services.DbServiceClientImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static lesson_22_jpql.demo.DbServiceDemo.HIBERNATE_CFG_FILE;

public abstract class AbstractHibernateTest {
    protected SessionFactory sessionFactory;
    protected TransactionManager transactionManager;
    protected DataTemplate<Client> clientTemplate;
    protected DBServiceClient dbServiceClient;

    private static final Logger log = LoggerFactory.getLogger(AbstractHibernateTest.class);


    private static TestContainersConfig.CustomPostgreSQLContainer container;

    @BeforeAll
    public static void init() {
        container = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        container.start();
    }

    @AfterAll
    public static void shutdown() {
        container.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(
                configuration, Client.class);
        transactionManager = new TransactionManagerHibernate(sessionFactory);

        clientTemplate = new DataTemplateHibernate<>(Client.class);

        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        addRecords();
        log.info("-------------------------");
    }

    private void addRecords(){
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

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }
}
