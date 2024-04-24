package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();
    private final TypeTasks type = TypeTasks.EPIC;

    public Epic(String name, String description) {
        super(name, description, TypeTasks.EPIC);
    }

    public List<Integer> getSubtasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return super.getId() + "," + TypeTasks.EPIC + "," +
                super.getName() + "," + super.getStatus() + "," + super.getDescription() + ",";
    }
}
