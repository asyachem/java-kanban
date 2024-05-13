package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    TaskManager taskManager;
    Task task;
    Subtask subtask;
    Epic epic;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task = new Task("Стирка","стираем белье");
        subtask = new Subtask("Стирка","стираем белье", 1);
        epic = new Epic("Стирка","стираем белье");
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void shouldAddTaskByTaskManagerAndFindItById() {
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    public void shouldAddSubtaskByTaskManagerAndFindItById() {
        assertEquals(subtask, taskManager.getSubTaskById(subtask.getId()));
    }

    @Test
    public void shouldAddEpicByTaskManagerAndFindItById() {
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
        Task currentTask = taskManager.getTaskById(task.getId());

        assertEquals(task.getName(), currentTask.getName());
        assertEquals(task.getDescription(), currentTask.getDescription());
        assertEquals(task.getStatus(), currentTask.getStatus());
    }

    @Test
    void shouldAddTask() {
        assertTrue(taskManager.getAllTasks().contains(task));
    }

    @Test
    void shouldUpdateTask() {
        task.setName("UPDATE");
        int taskId = task.getId();
        taskManager.updateTask(task);
        assertEquals(task, taskManager.getTaskById(taskId));
    }

    @Test
    void shouldClearTaskById() {
        int taskId = task.getId();
        taskManager.clearTaskById(taskId);
        assertFalse(taskManager.getAllTasks().contains(task));
    }

    @Test
    void shouldClearAllTasks() {
        taskManager.clearAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldAddSubtask() {
        assertTrue(taskManager.getAllSubtasks().contains(subtask));
    }

    @Test
    void shouldUpdateSubtask() {
        subtask.setName("UPDATE");
        int subtaskId = subtask.getId();
        taskManager.updateSubtask(subtask);
        assertEquals(subtask, taskManager.getSubTaskById(subtaskId));
    }

    @Test
    void shouldClearSubtaskById() {
        int subtaskId = subtask.getId();
        taskManager.clearSubtaskById(subtaskId);
        assertFalse(taskManager.getAllSubtasks().contains(subtask));
    }

    @Test
    void shouldClearAllSubtasks() {
        taskManager.clearAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void shouldAddEpic() {
        assertTrue(taskManager.getAllEpics().contains(epic));
    }

    @Test
    void shouldUpdateEpic() {
        epic.setName("UPDATE");
        int epicId = epic.getId();
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epicId));
    }

    @Test
    void shouldClearEpicById() {
        Epic epic1 = new Epic("Задача","описание");
        taskManager.addEpic(epic1);

        taskManager.clearEpicById(epic1.getId());

        assertFalse(taskManager.getAllEpics().contains(epic1));
    }

    @Test
    void shouldClearAllEpicsAndMakeEmptySubtasks() {
        taskManager.clearAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void shouldReturnHistory() {
        taskManager.getTaskById(0);
        assertNotNull(taskManager.getHistory());
    }

    @Test
    void shouldStatusEpicIsNewWhenAllSubtasksStatusNew() {
        Subtask subtask1 = new Subtask("Имя","Описание", 1);
        taskManager.addSubtask(subtask1);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void shouldStatusEpicIsDoneWhenAllSubtasksStatusDone() {
        Subtask subtask1 = new Subtask("Имя","Описание", 1);
        taskManager.addSubtask(subtask1);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void shouldStatusEpicInProgressWhenAllSubtasksStatusNewAndDone() {
        Subtask subtask1 = new Subtask("Имя","Описание", 1);
        taskManager.addSubtask(subtask1);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldStatusEpicInProgressWhenAllSubtasksStatusInProgress() {
        Subtask subtask1 = new Subtask("Имя","Описание", 1);
        taskManager.addSubtask(subtask1);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void testPrioritizedTaskSaveException() {
        Task task1 = new Task("task1","описание1", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        Task task2 = new Task("task2","описание2", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        taskManager.addTask(task1);

        assertThrows(PrioritizedTaskSaveException.class, () -> {
            taskManager.checkTaskTime(task2);
        });
    }

    @Test
    void testPrioritizedTaskSaveExceptionWhenTimeTasksNestedIntoEachOther() {
        Task task1 = new Task("task1","описание1", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        Task task2 = new Task("task2","описание2", 20, LocalDateTime.of(2024, 6, 5, 15, 10));
        taskManager.addTask(task1);

        assertThrows(PrioritizedTaskSaveException.class, () -> {
            taskManager.checkTaskTime(task2);
        });
    }

    @Test
    void testPrioritizedTaskSaveExceptionWhenTimeTaskAndSubtaskNestedIntoEachOther() {
        Task task1 = new Task("task1","описание1", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        Subtask sub = new Subtask("subtask","описание2", 1, 20, LocalDateTime.of(2024, 6, 5, 15, 10));
        taskManager.addTask(task1);

        assertThrows(PrioritizedTaskSaveException.class, () -> {
            taskManager.checkTaskTime(sub);
        });
    }

    @Test
    void shouldPrioritizedTasksBeSorted() {
        Task task1 = new Task("task1","описание1", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        Task task2 = new Task("task2","описание2", 20, LocalDateTime.of(2024, 7, 5, 15, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        LocalDateTime first = taskManager.getPrioritizedTasks().first().getStartTime();
        LocalDateTime last = taskManager.getPrioritizedTasks().last().getStartTime();

        assertTrue(first.isBefore(last));
    }
}