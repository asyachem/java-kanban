package history;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;
    int numberId = 0;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("Таск1","1");
        task1.setId(generateId());
        task2 = new Task("Таск2","2");
        task2.setId(generateId());
        task3 = new Task("Таск3","3");
        task3.setId(generateId());

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
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

    // проверяет что сохранит 3 таска
    @Test
    public void save3TasksInHistoryManager() {
        assertEquals(historyManager.getHistory().size(), 3);
    }

    // при вызове remove удаляет таск и размер на 1 меньше
    @Test
    public void deleteCurrentTaskAndSize3() {
        historyManager.remove(0);
        assertEquals(historyManager.getHistory().size(), 2);
    }

    // проверка на удаление из начала истории
    @Test
    public void deletingFromBeginningOfHistory() {
        historyManager.remove(0);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.contains(task2));
        assertTrue(history.contains(task3));
    }

    // проверка на удаление из середины истории
    @Test
    public void deletingFromMiddleOfHistory() {
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.contains(task1));
        assertTrue(history.contains(task3));
    }

    // проверка на удаление с конца истории
    @Test
    public void deletingFromEndOfHistory() {
        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }


}