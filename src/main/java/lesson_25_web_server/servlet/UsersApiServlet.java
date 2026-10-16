package lesson_25_web_server.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lesson_25_web_server.dto.ClientDtoReq;
import lesson_25_web_server.dto.ClientDtoResp;
import lesson_25_web_server.model.Client;
import lesson_25_web_server.services.DBServiceClient;
import lesson_25_web_server.utils.Constants;
import lesson_25_web_server.utils.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"java:S1989"})
public class UsersApiServlet extends HttpServlet {

    private final transient DBServiceClient dbServiceClient;

    public UsersApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (isPlainPath(pathInfo)) {
            List<ClientDtoResp> respList = dbServiceClient.findAll().stream()
                    .map(ClientDtoResp.createFromClient())
                    .toList();
            createJson(response, HttpServletResponse.SC_OK, respList);
            return;
        }

        long id = extractIdFromRequest(request);
        if (id <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
            return;
        }

        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        if (clientOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ClientDtoResp dtoResp = ClientDtoResp.createFromClient().apply(clientOptional.get());
        createJson(response, HttpServletResponse.SC_OK, dtoResp);
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (isPlainPath(pathInfo)) {
            return -1;
        }

        if (pathInfo.charAt(0) != '/') {
            return -1;
        }

        String raw = pathInfo.substring(1);
        if (raw.isEmpty() || raw.indexOf('/') >= 0) {
            return -1;
        }
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c < '0' || c > '9') {
                return -1;
            }
        }
        try {
            long id = Long.parseLong(raw);
            return (id > 0) ? id : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (!isPlainPath(pathInfo)) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "POST allowed only on /api/client");
            return;
        }

        ClientDtoReq dtoReq;
        try {
            dtoReq = JsonUtil.getMAPPER().readValue(request.getReader(), ClientDtoReq.class);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }

        if (isMissingField(dtoReq.firstName()) || isMissingField(dtoReq.login()) ||
                isMissingField(dtoReq.password())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing fields");
            return;
        }

        Client client = dbServiceClient.saveClient(
                new Client(dtoReq.firstName(), dtoReq.login(), dtoReq.password()));

        createJson(response, HttpServletResponse.SC_CREATED,
                ClientDtoResp.createFromClient().apply(client));
    }

    private boolean isPlainPath(String pathInfo){
        return (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/"));
    }

    private boolean isMissingField(String field){
        return field == null || field.isBlank();
    }

    private void createJson(HttpServletResponse response, int status, Object data) throws IOException {
        response.setContentType(Constants.get("json"));
        response.setStatus(status);
        response.getWriter().print(JsonUtil.getMAPPER().writeValueAsString(data));
    }
}
