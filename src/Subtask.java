public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
