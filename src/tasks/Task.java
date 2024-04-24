package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    protected final TypeTasks type;

    public Task(String name, String description) {
        this(name, description, TypeTasks.TASK);
    }

    public Task(String name, String description, TypeTasks type) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + TypeTasks.TASK + "," + name + "," + status + "," + description + ",";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public TypeTasks getType() {
        return type;
    }
}
