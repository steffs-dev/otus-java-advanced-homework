package lesson_25_web_server.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lesson_25_web_server.utils.Constants;

import java.io.IOException;

public class AuthorizationFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        this.context.log("Requested Resource:" + uri);

        HttpSession session = request.getSession(false);
        Object authenticated = (session == null) ? null :
                session.getAttribute(Constants.get("session.attribute"));
        if (session == null || !Boolean.TRUE.equals(authenticated)) {
            response.sendRedirect(Constants.get("uri.login"));
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
