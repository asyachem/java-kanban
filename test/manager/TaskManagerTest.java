package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        taskManager.addSubtask(subtask, epic);
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

}