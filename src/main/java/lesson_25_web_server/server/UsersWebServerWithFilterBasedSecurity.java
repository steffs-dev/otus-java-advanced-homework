package lesson_25_web_server.server;


import lesson_25_web_server.services.DBServiceClient;
import lesson_25_web_server.servlet.*;
import lesson_25_web_server.utils.Constants;
import lesson_25_web_server.utils.FileSystemHelper;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import lesson_25_web_server.services.TemplateProcessor;
import lesson_25_web_server.services.UserAuthService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity implements UsersWebServer {
    private final UserAuthService authService;

    private final DBServiceClient dbServiceClient;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerWithFilterBasedSecurity(
            int port, UserAuthService authService, DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.authService = authService;
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler,
                Constants.get("uri.client.all"),
                Constants.get("uri.client.by.id")));

        server.setHandler(sequence);
    }

    @Override
    public Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(
                new LoginServlet(templateProcessor, authService)), Constants.get("uri.login"));
        servletContextHandler.addServlet(new ServletHolder(
                new LogoutServlet()), Constants.get("uri.logout"));
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(
                                new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(Constants.get("page.start"));
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(Constants.get("common.res.dir")));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(
                new UsersApiServlet(dbServiceClient)), Constants.get("uri.client.all"));
        servletContextHandler.addServlet(new ServletHolder(
                new UsersApiServlet(dbServiceClient)), Constants.get("uri.client.by.id"));
        return servletContextHandler;
    }
}
