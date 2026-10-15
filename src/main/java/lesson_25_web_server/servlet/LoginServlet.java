package lesson_25_web_server.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lesson_25_web_server.dto.CredentialsDtoReq;
import lesson_25_web_server.services.TemplateProcessor;
import lesson_25_web_server.services.UserAuthService;
import lesson_25_web_server.utils.Constants;

import java.io.IOException;
import java.util.Collections;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@SuppressWarnings({"java:S1989"})
public class LoginServlet extends HttpServlet {

    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final transient TemplateProcessor templateProcessor;
    private final transient UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Constants.get("text.html"));
        response.getWriter().println(templateProcessor.getPage(Constants.get("page.login"), Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CredentialsDtoReq credentialsDtoReq = CredentialsDtoReq.builder()
                .login(request.getParameter(Constants.get("param.login")))
                .password(request.getParameter(Constants.get("param.pwd")))
                .build();

        if (userAuthService.authenticate(credentialsDtoReq)) {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.get("session.attribute"), Boolean.TRUE);
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(Constants.get("page.admin"));
        } else {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(Constants.get("text.html"));
            response.getWriter().println("Access Denied");
        }
    }
}
