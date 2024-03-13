package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {
    TaskManager taskManager;
    HistoryManager historyManager;
    Task task;
    Subtask subtask;
    Epic epic;

    @BeforeEach
    public void beforeEach() {
         taskManager = Managers.getDefault();
         historyManager = Managers.getDefaultHistory();
         task = new Task("Стирка","стираем белье");
         subtask = new Subtask("Стирка","стираем белье", 1);
         epic = new Epic("Стирка","стираем белье");
    }


    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    public void shouldReturnTaskManager() {
        assertNotNull(taskManager);
    }

    @Test
    public void shouldReturnHistoryManager() {
        assertNotNull(historyManager);
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void shouldAddTaskByTaskManagerAndFindItById() {
        taskManager.addTask(task);

        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    public void shouldAddSubtaskByTaskManagerAndFindItById() {
        taskManager.addSubtask(subtask, epic);

        assertEquals(subtask, taskManager.getSubTaskById(subtask.getId()));
    }

    @Test
    public void shouldAddEpicByTaskManagerAndFindItById() {
        taskManager.addEpic(epic);

        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void shouldOverwriteId() {
        task.setId(8);
        taskManager.addTask(task);

        assertNotNull(taskManager.getAllTasks());
    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void shouldNotChangeTasksFieldsWhenAddInTaskManager() {
        taskManager.addTask(task);
        Task currentTask = taskManager.getTaskById(task.getId());

        assertEquals(task.getName(), currentTask.getName());
        assertEquals(task.getDescription(), currentTask.getDescription());
        assertEquals(task.getStatus(), currentTask.getStatus());
    }

}