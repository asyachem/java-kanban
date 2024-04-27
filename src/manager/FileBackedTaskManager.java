package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    // методы по Task
    @Override
    public void clearAllTasks() throws ManagerSaveException {
        super.clearAllTasks();
        save();
    }
    @Override
    public void addTask(Task newTask) throws ManagerSaveException {
        super.addTask(newTask);
        save();
    }

    @Override
    public void updateTask(Task updateTask) throws ManagerSaveException {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void clearTaskById(int id) throws ManagerSaveException {
        super.clearTaskById(id);
        save();
    }

    //  методы для Subtask
    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void clearAllSubtasks() throws ManagerSaveException {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask updateTask) throws ManagerSaveException {
        super.updateSubtask(updateTask);
        save();
    }

    @Override
    public void clearSubtaskById(Integer id) throws ManagerSaveException {
        super.clearSubtaskById(id);
        save();
    }

    // методы для Epic
    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void clearAllEpics() throws ManagerSaveException {
        super.clearAllEpics();
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) throws ManagerSaveException {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void clearEpicById(int id) throws ManagerSaveException {
        super.clearEpicById(id);
        save();
    }

    public void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter("tasks.csv")) {
            writer.write("id,type,name,status,description,epic");
            writer.write("\n");
            for (Task task : super.getAllTasks()) {
                writer.write(task.toString());
                writer.write("\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                writer.write(subtask.toString());
                writer.write("\n");
            }
            for (Epic epic : super.getAllEpics()) {
                writer.write(epic.toString());
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время создания файла.");
        }
    }

    public static FileBackedTaskManager loadFromFile()  {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        try {
            List<String> lines = Files.readAllLines(Paths.get("tasks.csv"));
            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                if (task.getType() == TypeTasks.TASK) {
                    taskManager.tasks.put(task.getId(), task);
                } else if (task.getType() == TypeTasks.SUBTASK) {
                    taskManager.subTasks.put(task.getId(), (Subtask) task);
                } else if (task.getType() == TypeTasks.EPIC) {
                    taskManager.epics.put(task.getId(), (Epic) task);
                }
            }
        } catch (IOException ignored) {
        }
        return taskManager;
    }

    private static Task fromString(String value) {
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
