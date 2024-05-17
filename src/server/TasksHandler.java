package server;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.PrioritizedTaskSaveException;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
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

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        // если нет айди то метод addTask, иначе updateTask
        InputStream inputStream = exchange.getRequestBody();
        String taskJson = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(taskJson, Task.class);

        // если передать в запросе объект с id null, то id присваивает 0, как исправить, чтобы было null?
        Optional<Integer> id = Optional.of(task.getId());
        if (id.isEmpty()) {
            try {
                taskManager.addTask(task);
                sendText(exchange, "Добавили новую задачу", 201);
                return;
            } catch (PrioritizedTaskSaveException e) {
                sendHasInteractions(exchange);
            }
        }

        //обновление задачи
        try {
            taskManager.updateTask(task);
        } catch (PrioritizedTaskSaveException e) {
            sendHasInteractions(exchange);
        }
        sendText(exchange, "Обновили задачу", 201);
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(exchange);
        if (taskIdOpt.isEmpty()) {
            throw new NotFoundException("Некорректный идентификатор задачи");
        }
        int taskId = taskIdOpt.get();

        taskManager.clearTaskById(taskId);
        sendText(exchange, "Задача была удалена", 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int taskId = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);

        Optional<Task> taskOpt = Optional.ofNullable(taskManager.getTaskById(taskId));
        if (taskOpt.isEmpty()) {
            sendNotFound(exchange, "Такой задачи нет", 404);
        }
        Task task = taskOpt.get();

        String jsonTask = gson.toJson(task);
        sendText(exchange, jsonTask, 200);
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        ArrayList<Task> tasks = taskManager.getAllTasks();

        String jsonArray = gson.toJson(tasks);

        sendText(exchange, jsonArray, 200);
    }

    Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length < 3) {
            return Optional.empty();
        } else {
            return Optional.of(Integer.parseInt(pathParts[2]));
        }
    }
}
