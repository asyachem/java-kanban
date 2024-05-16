package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.PrioritizedTaskSaveException;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

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
        ArrayList<Subtask> subtasks = taskManager.getAllSubtasks();

        String jsonArray = this.gson.toJson(subtasks);

        sendText(exchange, jsonArray, 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int subtaskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);

        Optional<Subtask> subtaskOpt = Optional.ofNullable(taskManager.getSubTaskById(subtaskId));
        if (subtaskOpt.isEmpty()) {
            sendNotFound(exchange, "Такой задачи нет", 404);
        }
        Subtask subtask = subtaskOpt.get();

        String jsonSubtask = this.gson.toJson(subtask);
        sendText(exchange, jsonSubtask, 200);
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String subtaskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(subtaskJson, Subtask.class);

        Optional<Integer> id = Optional.of(subtask.getId());
        if (id.isEmpty()) {
            try {
                taskManager.addSubtask(subtask);
                sendText(exchange, "Добавили новую задачу", 201);
                return;
            } catch (PrioritizedTaskSaveException e) {
                sendHasInteractions(exchange);
            }
        }

        //обновление задачи
        try {
            taskManager.updateSubtask(subtask);
        } catch (PrioritizedTaskSaveException e) {
            sendHasInteractions(exchange);
        }
        sendText(exchange, "Обновили задачу", 201);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> subtaskIdOpt = getTaskId(exchange);
        if (subtaskIdOpt.isEmpty()) {
            throw new NotFoundException("Некорректный идентификатор задачи");
        }
        int subtaskId = subtaskIdOpt.get();

        taskManager.clearSubtaskById(subtaskId);
        sendText(exchange, "Задача была удалена", 200);
    }

    Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length < 2) {
            return Optional.empty();
        } else {
            return Optional.of(Integer.parseInt(pathParts[2]));
        }
    }
}