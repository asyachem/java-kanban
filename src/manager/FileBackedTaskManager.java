package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private List<Task> tasks = new ArrayList<>();


    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        tasks.add(newTask);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask, Epic epic){
        super.addSubtask(subtask, epic);
        tasks.add(subtask);
        save();
    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        tasks.add(newEpic);
        save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter("tasks.csv")) {
            writer.write("id,type,name,status,description,epic");
            writer.write("\n");
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время создания файла.");
        }
    }

    @Override
    public void loadFromFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("tasks.csv"));
        for (int i = 1; i < lines.size(); i++) {
            Task task = fromString(lines.get(i));
            if (task.getType() == TypeTasks.TASK) {
                addTask(task);
            } else if (task.getType() == TypeTasks.SUBTASK) {
                addSubtask((Subtask) task, getEpicById(((Subtask) task).getIdEpic()));
            } else if (task.getType() == TypeTasks.EPIC) {
                addEpic((Epic) task);
            }
        }
    }

    private Task fromString(String value) {
        String[] str = value.split(",");
        Task task = null;
        if (TypeTasks.TASK.toString().equals(str[1])) {
            task = new Task(str[2], str[4]);
        } else if (TypeTasks.SUBTASK.toString().equals(str[1])) {
            task = new Subtask(str[2], str[4], Integer.valueOf(str[5]));
        } else if (TypeTasks.EPIC.toString().equals(str[1])) {
            task = new Epic(str[2], str[4]);
        }
        task.setId(Integer.valueOf(str[0]));
        task.setStatus(Status.valueOf(str[3]));
        return task;
    }
}
