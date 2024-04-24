package tasks;

public class Subtask extends Task {
    private int idEpic;

    public Subtask(String name, String description, int idEpic) {
        super(name, description, TypeTasks.SUBTASK);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return super.getId() + "," + TypeTasks.SUBTASK + "," +
                super.getName() + "," + super.getStatus() + "," + super.getDescription() + "," + idEpic + ",";
    }
}
