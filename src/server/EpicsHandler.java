package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;
import tasks.PrioritizedTaskSaveException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        try {
            switch (endpoint) {
                case GET_TASKS: {
                    handleGetTasks(exchange);
                    break;
                }
                case GET_TASK_BY_ID: {
                    handleGetTaskById(exchange);
                    break;
                }
                case POST_TASK: {
                    handlePostTasks(exchange);
                    break;
                }
                case DELETE_TASK: {
                    handleDeleteTask(exchange);
                    break;
                }
                case UNKNOWN:
                    sendNotFound(exchange, "Такого эндпоинта не существует", 404);
                    break;
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, "Некорректный идентификатор задачи", 400);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        ArrayList<Epic> epics = taskManager.getAllEpics();

        String jsonArray = gson.toJson(epics);

        sendText(exchange, jsonArray, 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int epicId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);

        Optional<Epic> epicOpt = Optional.ofNullable(taskManager.getEpicById(epicId));
        if (epicOpt.isEmpty()) {
            sendNotFound(exchange, "Такого эпика нет", 404);
        }
        Epic epic = epicOpt.get();

        String jsonTask = gson.toJson(epic);
        sendText(exchange, jsonTask, 200);
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String epicJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(epicJson, Epic.class);

        Optional<Integer> id = Optional.ofNullable(epic.getId());
        if (id.isEmpty()) {
            try {
                taskManager.addEpic(epic);
                sendText(exchange, "Добавили новый эпик", 201);
                return;
            } catch (PrioritizedTaskSaveException e) {
                sendHasInteractions(exchange);
            }
        }

        //обновление
        try {
            taskManager.updateEpic(epic);
        } catch (PrioritizedTaskSaveException e) {
            sendHasInteractions(exchange);
        }
        sendText(exchange, "Обновили эпик", 201);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> epicIdOpt = getTaskId(exchange);
        if (epicIdOpt.isEmpty()) {
            throw new NotFoundException("Некорректный идентификатор задачи");
        }
        int epicId = epicIdOpt.get();

        taskManager.clearEpicById(epicId);
        sendText(exchange, "Эпик был удален", 200);
    }

    Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length < 2) {
            System.out.println('3');
            return Optional.empty();
        } else {
            return Optional.of(Integer.parseInt(pathParts[2]));
        }
    }
}