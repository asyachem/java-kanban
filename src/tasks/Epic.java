package tasks;

import manager.InMemoryTaskManager;

import java.time.LocalDateTime;
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

    public LocalDateTime getStartTime(InMemoryTaskManager manager) {
        List<Subtask> listSubtasks = manager.getListSubtasksofEpic(this);
        LocalDateTime startTime = null;
        for (Subtask s : listSubtasks) {
            if (s.getStartTime() != null && startTime == null) {
                startTime = s.getStartTime();
                continue;
            }
            if (s.getStartTime() != null && startTime != null) {
                if (s.getStartTime().isBefore(startTime)) {
                    startTime = s.getStartTime();
                }
            }
        }
        return startTime;
    }

    public LocalDateTime getEndTime(InMemoryTaskManager manager) {
        LocalDateTime startTime = this.getStartTime(manager);
        List<Subtask> listSubtasks = manager.getListSubtasksofEpic(this);
        for (Subtask s : listSubtasks) {
            if(s.getDuration() != null) {
                startTime.plus(s.getDuration());
            }
        }
        return startTime;
    }
}
