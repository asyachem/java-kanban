package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        String fileTest = "test.csv";
        FileBackedTaskManager anotherManager = new FileBackedTaskManager(fileTest).loadFromFile();
        task1 = new Task("Стирка","стираем белье");
        task2 = new Task("Глажка","гладим белье");

        anotherManager.addTask(task1);
        anotherManager.addTask(task2);

        epic = new Epic("Глобальная уборка", "на выходных");
        anotherManager.addEpic(epic);
        sub1 = new Subtask("Мыть полы", "выходные", epic.getId());
        sub2 = new Subtask("Пропылесосить", "выходные", epic.getId());
        anotherManager.addSubtask(sub1);
        anotherManager.addSubtask(sub2);

        epic2 = new Epic("Переезд", "переезд на дачу");
        anotherManager.addEpic(epic2);
        sub3 = new Subtask("Морально подготовиться", "когда-нибудь", epic2.getId());
        anotherManager.addSubtask(sub3);

        List<String> file1 = Files.readAllLines(Paths.get("tasks.csv"));
        List<String> file2 = Files.readAllLines(Paths.get("test.csv"));
        for (int i = 1; i < file1.size(); i++) {
            assertEquals(file1.get(i), file2.get(i));
        }
    }
}
