package history;

import tasks.Task;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();

}
