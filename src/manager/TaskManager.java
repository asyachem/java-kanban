package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    // методы по Task
    ArrayList<Task> getAllTasks();
    void clearAllTasks();
    Task getTaskById(int id);
    void addTask(Task newTask);
    void updateTask(Task updateTask);
    void clearTaskById(int id);

    //методы по Subtask
    ArrayList<Subtask> getAllSubtasks();
    List<Subtask> getListSubtasksofEpic(Epic epic);
    void clearAllSubtasks();
    Subtask getSubTaskById(int id);
    void addSubtask(Subtask subtask);
    void updateSubtask(Subtask updateTask);
    void clearSubtaskById(Integer id);

    // методы по Epic
    ArrayList<Epic> getAllEpics();
    void clearAllEpics();
    Epic getEpicById(int id);
    void addEpic(Epic newEpic);
    void updateEpic(Epic updateEpic);
    void clearEpicById(int id);

    List<Task> getHistory();
    TreeSet<Task> getPrioritizedTasks();
    void checkTaskTime(Task task);
}
