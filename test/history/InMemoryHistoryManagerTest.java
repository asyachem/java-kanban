package history;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    Task task1;
    Task task6;
    Task task11;
    int numberId = 0;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("Таск1","1");
        task1.setId(generateId());
        Task task2 = new Task("Таск2","2");
        task2.setId(generateId());
        Task task3 = new Task("Таск3","3");
        task3.setId(generateId());
        Task task4 = new Task("Таск4","4");
        task4.setId(generateId());
        Task task5 = new Task("Таск5","5");
        task5.setId(generateId());
        task6 = new Task("Таск6","6");
        task6.setId(generateId());
        Task task7 = new Task("Таск7","7");
        task7.setId(generateId());
        Task task8 = new Task("Таск8","8");
        task8.setId(generateId());
        Task task9 = new Task("Таск9","9");
        task9.setId(generateId());
        Task task10 = new Task("Таск10","10");
        task10.setId(generateId());
        task11 = new Task("Таск11","11");
        task11.setId(generateId());

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
        historyManager.add(task11);
    }

    private int generateId() {
        return numberId++;
    }


    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void shouldNotChangeWhenAddInHistoryManager() {
        Task currentTask = historyManager.getHistory().get(0);

        assertEquals(task1.getName(), currentTask.getName());
        assertEquals(task1.getDescription(), currentTask.getDescription());
        assertEquals(task1.getStatus(), currentTask.getStatus());
    }

    // проверяет что больше 10 тасков добавятся в HistoryManager
    @Test
    public void save10TasksInHistoryManager() {
        assertEquals(historyManager.getHistory().size(), 11);
    }

    // при вызове remove удаляет таск и размер на 1 меньше
    @Test
    public void deleteCurrentTaskAndSize10() {
        historyManager.remove(0);
        assertEquals(historyManager.getHistory().size(), 10);
    }

    // проверка на удаление из начала истории
    @Test
    public void deletingFromBeginningOfHistory() {
        historyManager.remove(0);

        List<Task> history = historyManager.getHistory();

        assertFalse(history.contains(task1));
    }

    // проверка на удаление из середины истории
    @Test
    public void deletingFromMiddleOfHistory() {
        historyManager.remove(5);

        List<Task> history = historyManager.getHistory();

        assertFalse(history.contains(task6));
    }

    // проверка на удаление с конца истории
    @Test
    public void deletingFromEndOfHistory() {
        historyManager.remove(10);

        List<Task> history = historyManager.getHistory();

        assertFalse(history.contains(task11));
    }


}