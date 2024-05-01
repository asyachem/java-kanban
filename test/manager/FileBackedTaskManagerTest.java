package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    TaskManager taskManager;
    Task task1;
    Task task2;
    Subtask sub1;
    Subtask sub2;
    Subtask sub3;
    Epic epic;
    Epic epic2;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();

        task1 = new Task("Стирка","стираем белье");
        task2 = new Task("Глажка","гладим белье");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        epic = new Epic("Глобальная уборка", "на выходных");
        taskManager.addEpic(epic);
        sub1 = new Subtask("Мыть полы", "выходные", epic.getId());
        sub2 = new Subtask("Пропылесосить", "выходные", epic.getId());
        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        epic2 = new Epic("Переезд", "переезд на дачу");
        taskManager.addEpic(epic2);
        sub3 = new Subtask("Морально подготовиться", "когда-нибудь", epic2.getId());
        taskManager.addSubtask(sub3);
    }

    @Test
    public void shouldReturnTask() throws IOException {
        FileBackedTaskManager anotherManager = FileBackedTaskManager.loadFromFile("tasks.csv");

        assertEquals(taskManager.getAllTasks(), anotherManager.getAllTasks());
        assertEquals(taskManager.getAllEpics(), anotherManager.getAllEpics());
        assertEquals(taskManager.getAllSubtasks(), anotherManager.getAllSubtasks());
    }
}
