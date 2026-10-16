package lesson_25_web_server.server;

import lesson_25_web_server.base.AbstractHibernateTest;
import lesson_25_web_server.services.*;
import lesson_25_web_server.utils.Constants;
import org.junit.jupiter.api.Test;


class UsersWebServerWithFilterBasedSecurityTest extends AbstractHibernateTest {

    @Test
    void testServer() throws Exception {
        final int WEB_SERVER_PORT = 8081;
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(Constants.get("templates.dir"));
        UserAuthService authService = new UserAuthServiceImpl(dbServiceClient);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}