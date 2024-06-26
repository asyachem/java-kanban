package manager;


import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager("tasks.csv");
    }
}
