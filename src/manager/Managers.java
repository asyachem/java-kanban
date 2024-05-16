package manager;


import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    private static TaskManager taskManager;
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        if(taskManager == null){
            taskManager = FileBackedTaskManager.loadFromFile("tasks.csv");
        }
        return taskManager;
    }
}
