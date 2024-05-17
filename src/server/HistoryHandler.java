package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_HISTORY: {
                handleGetHistory(exchange);
                break;
            }
            case UNKNOWN:
                sendNotFound(exchange, "Такого эндпоинта не существует", 404);
                break;
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();

        String jsonArray = gson.toJson(history);

        sendText(exchange, jsonArray, 200);
    }
}