package manager;

import tasks.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {
    Comparator<Task> taskComparator = new TaskComparator();
    TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    String filePath;

    public FileBackedTaskManager(String filePath) {
        super();
        this.filePath = filePath;
    }

    // методы по Task
    @Override
    public void clearAllTasks() {
        ArrayList<Task> tasks = super.getAllTasks();
        for (Task t : tasks) {
            if (this.prioritizedTasks.contains(t)) {
                this.prioritizedTasks.remove(t);
            }
        }
        super.clearAllTasks();
        save();
    }
    @Override
    public void addTask(Task newTask) {
        if (newTask.getDuration() != null) {
            try {
                checkTaskTime(newTask);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.add(newTask);
        }
        super.addTask(newTask);
        save();
    }


    @Override
    public void updateTask(Task updateTask) {
        if (updateTask.getDuration() != null) {
            try {
                checkTaskTime(updateTask);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.remove(super.getTaskById(updateTask.getId()));
            this.prioritizedTasks.add(updateTask);
        }
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void clearTaskById(int id) {
        Task task = super.getTaskById(id);
        if (this.prioritizedTasks.contains(task)) {
            this.prioritizedTasks.remove(task);
        }
        super.clearTaskById(id);
        save();
    }

    //  методы для Subtask
    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getDuration() != null) {
            try {
                checkTaskTime(subtask);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.add(subtask);
        }
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void clearAllSubtasks() {
        ArrayList<Subtask> subtasks = super.getAllSubtasks();
        for (Task t : subtasks) {
            if (this.prioritizedTasks.contains(t)) {
                this.prioritizedTasks.remove(t);
            }
        }
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask updateTask) {
        if (updateTask.getDuration() != null) {
            try {
                checkTaskTime(updateTask);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.remove(super.getSubTaskById(updateTask.getId()));
            this.prioritizedTasks.add(updateTask);
        }
        super.updateSubtask(updateTask);
        save();
    }

    @Override
    public void clearSubtaskById(Integer id) {
        Subtask subtask = super.getSubTaskById(id);
        if (this.prioritizedTasks.contains(subtask)) {
            this.prioritizedTasks.remove(subtask);
        }
        super.clearSubtaskById(id);
        save();
    }

    private void checkTaskTime(Task task) {
        List<Subtask> subtasks = super.getAllSubtasks();
        List<Task> tasks = super.getAllTasks();
        for (Subtask t : subtasks) {
            if ((t.getStartTime().equals(task.getStartTime()) && t.getEndTime().equals(task.getEndTime())) ||
                    t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getStartTime()) ||
                    task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getStartTime()) ||
                    t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime()) ||
                    task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime())
            ) {
                throw new RuntimeException("Уже есть сабтаск с таким временем");
            }
        }
        for (Task t : tasks) {
            if ((t.getStartTime().equals(task.getStartTime()) && t.getEndTime().equals(task.getEndTime())) ||
                    t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getStartTime()) ||
                    task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getStartTime()) ||
                    t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime()) ||
                    task.getStartTime().isBefore(t.getStartTime()) && task.getEndTime().isAfter(t.getEndTime())
            ) {
                throw new RuntimeException("Уже есть таск с таким временем");
            }
        }
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

    public TreeSet<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }
}

class TaskComparator implements Comparator<Task> {

    public int compare(Task a, Task b) {
        if ((a.getStartTime()).isBefore(b.getStartTime())) {
            return -1;
        } else if ((a.getStartTime()).isAfter(b.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    }
}