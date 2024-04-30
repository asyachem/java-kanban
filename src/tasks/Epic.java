package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubtasks() {
        return subTasks;
    }

    public TypeTasks getType() {
        return TypeTasks.EPIC;
    }

    @Override
    public String toString() {
        return super.getId() + "," + TypeTasks.EPIC + "," +
                super.getName() + "," + super.getStatus() + "," + super.getDescription() + ",";
    }
}
