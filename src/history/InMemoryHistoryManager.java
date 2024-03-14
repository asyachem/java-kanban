package history;

import tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_SIZE_ARRAYLIST = 10;

    private final ArrayList<Task> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            checkHistorySize();
            historyTasks.add(task);
        }
    }

     private void checkHistorySize() {
        if (historyTasks.size() == MAX_SIZE_ARRAYLIST) {
            historyTasks.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyTasks);
    }
}
