package server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTasksTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.clearAllTasks();
        manager.clearAllSubtasks();
        manager.clearAllEpics();
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getAllTasks();
        System.out.println(tasksFromManager);
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addTask(task);

        Task task1 = new Task("Test", "Testing", 5, LocalDateTime.of(2000, 6, 10, 10, 0));
        task1.setId(0);
        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(tasksFromManager.size(), 1);
        assertEquals(tasksFromManager.get(0).getName(), task1.getName());
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(task.getName(), tasks[0].getName());
    }

    @Test
    public void testDeleteTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(manager.getAllTasks().size(), 0);
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing task 2", 0, 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("Test", "Testing", 0, 5, LocalDateTime.of(2000, 6, 10, 10, 0));
        subtask1.setId(1);
        String taskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertEquals(subtasksFromManager.size(), 1);
        assertEquals(subtasksFromManager.get(0).getName(), subtask1.getName());
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing task 2", 0, 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(subtask.getName(), subtasks[0].getName());
    }

    @Test
    public void testDeleteSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Test 2", "Testing task 2", 0, 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(manager.getAllSubtasks().size(), 0);
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertEquals(epicsFromManager.size(), 1);
        assertEquals(epicsFromManager.get(0).getName(), epic.getName());
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(epic.getName(), epics[0].getName());
    }

    @Test
    public void testDeleteEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");
        manager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(manager.getAllSubtasks().size(), 0);
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        manager.addTask(task);
        manager.getTaskById(0);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] task1 = gson.fromJson(response.body(), Task[].class);
        assertEquals(task.getName(), task1[0].getName());
    }

    @Test
    public void testGetPrTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 6, 10, 10, 0));
        Task task1 = new Task("Test 2", "Testing task 2", 5, LocalDateTime.of(2024, 7, 10, 10, 0));
        manager.addTask(task);
        manager.addTask(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(task.getName(), tasks[0].getName());
        assertEquals(task1.getName(), tasks[1].getName());
    }
}
