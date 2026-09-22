package lesson_19_jdbc.otus;

import lesson_19_jdbc.otus.core.repository.executor.DbExecutorImpl;
import lesson_19_jdbc.otus.core.sessionmanager.TransactionRunnerJdbc;
import lesson_19_jdbc.otus.crm.datasource.DriverManagerDataSource;
import lesson_19_jdbc.otus.crm.model.Client;
import lesson_19_jdbc.otus.crm.model.Manager;
import lesson_19_jdbc.otus.crm.service.DbServiceClientImpl;
import lesson_19_jdbc.otus.crm.service.DbServiceManagerImpl;
import lesson_19_jdbc.otus.jdbc.mapper.*;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        log.info("resource = {}", HomeWork.class.getClassLoader().getResource("db/migration"));

        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<Client>(){};
        EntitySQLMetaData<Client> entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(
                dbExecutor, entitySQLMetaDataClient); // реализация DataTemplate, универсальная

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<Manager>(){};
        EntitySQLMetaData<Manager> entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        managerSecond.setParam1("someParam1");
        System.out.println("managerSecond = " + managerSecond);
        dbServiceManager.saveManager(managerSecond);
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        Flyway.configure()
                .dataSource(dataSource)
                .locations("filesystem:C:/Users/Konstantin/IdeaProjects/otus_advanced/src/main/resources/db/migration")
                .loggers("slf4j")
                .load().migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
