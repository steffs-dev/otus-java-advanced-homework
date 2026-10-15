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

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient DBServiceClient dbServiceClient;

    public UsersApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            List<ClientDtoResp> respList = dbServiceClient.findAll().stream()
                    .map(ClientDtoResp.createFromClient())
                    .toList();
            response.setContentType(Constants.get("json"));
            response.getWriter().print(JsonUtil.getMAPPER().writeValueAsString(respList));
            return;
        }

        long id = extractIdFromRequest(request);
        if (id < 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id");
            return;
        }

        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        if (clientOptional.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ClientDtoResp dtoResp = ClientDtoResp.createFromClient().apply(clientOptional.get());
        response.setContentType(Constants.get("json"));
        response.getWriter().print(JsonUtil.getMAPPER().writeValueAsString(dtoResp));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            return -1;
        }
        try {
            String[] path = pathInfo.split("/");
            String id = (path.length > ID_PATH_PARAM_POSITION) ?
                    path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientDtoReq dtoReq;
        try {
            dtoReq = JsonUtil.getMAPPER().readValue(request.getReader(), ClientDtoReq.class);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }

        if (dtoReq.firstName() == null || dtoReq.login() == null || dtoReq.password() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing fields");
            return;
        }

        Client client = dbServiceClient.saveClient(
                new Client(dtoReq.firstName(), dtoReq.login(), dtoReq.password()));

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType(Constants.get("json"));
        response.getWriter().println(JsonUtil.getMAPPER().writeValueAsString(
                ClientDtoResp.createFromClient().apply(client)));
    }
}
