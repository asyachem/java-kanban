package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

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

        String jsonArray = gson.toJson(tasks);

        sendText(exchange, jsonArray, 200);
    }
}