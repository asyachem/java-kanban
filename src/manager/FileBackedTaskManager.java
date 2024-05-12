package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String filePath;

    public FileBackedTaskManager(String filePath) {
        super();
        this.filePath = filePath;
    }

    // методы по Task
    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }
    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }


    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void clearTaskById(int id) {
        super.clearTaskById(id);
        save();
    }

    //  методы для Subtask
    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask updateTask) {
        super.updateSubtask(updateTask);
        save();
    }

    @Override
    public void clearSubtaskById(Integer id) {
        super.clearSubtaskById(id);
        save();
    }

    // методы для Epic
    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void clearEpicById(int id) {
        super.clearEpicById(id);
        save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(this.filePath)) {
            writer.write("id,type,name,status,description,epic,startTime,duration");
            writer.write("\n");
            for (Task task : super.getAllTasks()) {
                writer.write(task.toString());
                writer.write("\n");
            }
            for (Epic epic : super.getAllEpics()) {
                writer.write(epic.toString());
                writer.write("\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                writer.write(subtask.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время создания файла.");
        }
    }

    public static FileBackedTaskManager loadFromFile(String filePath)  {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(filePath);
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                switch (task.getType()) {
                    case TASK:
                        taskManager.tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        taskManager.subTasks.put(task.getId(), (Subtask) task);
                        Epic e = taskManager.getEpicById(((Subtask) task).getIdEpic());
                        e.getSubtasks().add(((Subtask) task).getId());
                        break;
                    case EPIC:
                        taskManager.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
        return taskManager;
    }

    private static Task fromString(String value) {
        String[] str = value.split(",");
        Task task = null;
        if (TypeTasks.TASK.toString().equals(str[1])) {
            if (!str[5].equals(null) && str[6].equals(null)) {
                task = new Task(str[2], str[4], Long.parseLong(str[5]), LocalDateTime.parse(str[6]));
            } else {
                task = new Task(str[2], str[4]);
            }
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