package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_PRIORITIZED_TASKS: {
                handleGetPrioritizedTasks(exchange);
                break;
            }
            case UNKNOWN:
                sendNotFound(exchange, "Такого эндпоинта не существует", 404);
                break;
        }
    }

    private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        TreeSet<Task> tasks = taskManager.getPrioritizedTasks();

        String jsonArray = this.gson.toJson(tasks);

        sendText(exchange, jsonArray, 200);
    }
}