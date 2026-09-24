package lesson_22_jpql.ru.otus.homework;

import lesson_22_jpql.core.repository.DataTemplateHibernate;
import lesson_22_jpql.core.repository.HibernateUtils;
import lesson_22_jpql.core.sessionmanager.TransactionManagerHibernate;
import lesson_22_jpql.crm.dbmigrations.MigrationsExecutorFlyway;
import lesson_22_jpql.crm.model.Address;
import lesson_22_jpql.crm.model.Client;
import lesson_22_jpql.crm.model.Phone;
import lesson_22_jpql.crm.service.DBServiceClient;
import lesson_22_jpql.crm.service.DbServiceClientImpl;
import lesson_22_jpql.ru.otus.base.AbstractHibernateTest;
import lesson_22_jpql.ru.otus.base.TestContainersConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Table;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.stream.StreamSupport;

import static lesson_22_jpql.demo.DbServiceDemo.HIBERNATE_CFG_FILE;

public abstract class AbstractHibernateHomeworkTest extends AbstractHibernateTest {
    protected Configuration configuration;
    private Metadata metadata;

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

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.executeMigrations();

        configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        metadata = HibernateUtils.getMetadata(configuration, Client.class, Address.class, Phone.class);
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        transactionManager = new TransactionManagerHibernate(sessionFactory);
        clientTemplate = new DataTemplateHibernate<>(Client.class);
        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }

    protected List<Table> getTables() {
        return StreamSupport.stream(metadata.getDatabase().getNamespaces().spliterator(),false)
                .flatMap(namespace -> namespace.getTables().stream())
                .toList();
    }
}
