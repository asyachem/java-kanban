package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    protected final TaskManager taskManager;

/*    public BaseHttpHandler() {
        this.taskManager = FileBackedTaskManager.loadFromFile("tasks.csv");
    }*/

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange h, String text, int responseCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(responseCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(406, 0);
            os.write("Уже есть задача с таким временем".getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_TASK;
        } else if (requestMethod.equals("POST")) {
            return Endpoint.POST_TASK;
        } else if (requestMethod.equals("GET") && pathParts.length == 2 && (pathParts[1].equals("tasks") || pathParts[1].equals("subtasks") || pathParts[1].equals("epics"))) {
            return Endpoint.GET_TASKS;
        } else if (requestMethod.equals("GET") && pathParts.length > 2 && (pathParts[1].equals("tasks") || pathParts[1].equals("subtasks") || pathParts[1].equals("epics"))) {
            return Endpoint.GET_TASK_BY_ID;
        } else if (requestMethod.equals("GET") && pathParts[1].equals("prioritized")) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        } else if (requestMethod.equals("GET") && pathParts[1].equals("history")) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.UNKNOWN;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {}

    public static Gson getGson() {
        return gson;
    }
}
