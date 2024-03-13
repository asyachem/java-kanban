package history;

import tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        checkHistorySize();
        historyTasks.add(task);
    }

     public void checkHistorySize() {
        if (historyTasks.size() == 10) {
            historyTasks.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(historyTasks, that.historyTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyTasks);
    }
}
