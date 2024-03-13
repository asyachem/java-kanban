package history;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    Task task1;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("Таск1","1");
        Task task2 = new Task("Таск2","2");
        Task task3 = new Task("Таск","3");
        Task task4 = new Task("Таск","4");
        Task task5 = new Task("Таск","5");
        Task task6 = new Task("Таск","6");
        Task task7 = new Task("Таск","7");
        Task task8 = new Task("Таск","8");
        Task task9 = new Task("Таск","9");
        Task task10 = new Task("Таск10","10");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);
    }



    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void shouldNotChangeWhenAddInHistoryManager() {
        Task currentTask = historyManager.getHistory().get(0);

        assertEquals(task1.getName(), currentTask.getName());
        assertEquals(task1.getDescription(), currentTask.getDescription());
        assertEquals(task1.getStatus(), currentTask.getStatus());
    }

    // проверяет что 10 тасков добавятся в HistoryManager
    @Test
    public void save10TasksInHistoryManager() {
        assertEquals(historyManager.getHistory().size(), 10);
    }

    // при добавлении 10 таска удаляет первый таск и сохраняет новый
    @Test
    public void save11TaskAndDeleteFirstTask() {
        Task task11 = new Task("Таск11","11");
        historyManager.add(task11);

        Task currentFirstTask = historyManager.getHistory().get(0);
        Task currentTenTask = historyManager.getHistory().get(9);

        assertEquals(currentFirstTask.getName(), "Таск2");
        assertEquals(currentFirstTask.getDescription(), "2");

        assertEquals(currentTenTask.getName(), "Таск11");
        assertEquals(currentTenTask.getDescription(), "11");
    }
}