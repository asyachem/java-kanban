package manager;


import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    private static final String filePath = "tasks.csv";
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(filePath);
    }
}
