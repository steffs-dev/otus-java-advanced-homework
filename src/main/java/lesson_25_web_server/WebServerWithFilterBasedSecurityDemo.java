package lesson_25_web_server;

import lesson_25_web_server.repository.DBInit;
import lesson_25_web_server.server.UsersWebServer;
import lesson_25_web_server.server.UsersWebServerWithFilterBasedSecurity;
import lesson_25_web_server.services.*;
import lesson_25_web_server.utils.Constants;

public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8081;

    public static void main(String[] args) throws Exception {
        DBInit dbInit = new DBInit();
        DBServiceClient dbServiceClient = dbInit.getDbServiceClient();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(Constants.get("templates.dir"));
        UserAuthService authService = new UserAuthServiceImpl(dbServiceClient);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
