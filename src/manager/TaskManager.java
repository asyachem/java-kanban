package manager;

import tasks.Epic;
import tasks.ManagerSaveException;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    // методы по Task
    ArrayList<Task> getAllTasks();
    void clearAllTasks() throws ManagerSaveException;
    Task getTaskById(int id);
    void addTask(Task newTask) throws ManagerSaveException;
    void updateTask(Task updateTask) throws ManagerSaveException;
    void clearTaskById(int id) throws ManagerSaveException;

    //методы по Subtask
    ArrayList<Subtask> getAllSubtasks();
    List<Subtask> getListSubtasksofEpic(Epic epic);
    void clearAllSubtasks() throws ManagerSaveException;
    Subtask getSubTaskById(int id);
    void addSubtask(Subtask subtask) throws ManagerSaveException;
    void updateSubtask(Subtask updateTask) throws ManagerSaveException;
    void clearSubtaskById(Integer id) throws ManagerSaveException;

    // методы по Epic
    ArrayList<Epic> getAllEpics();
    void clearAllEpics() throws ManagerSaveException;
    Epic getEpicById(int id);
    void addEpic(Epic newEpic) throws ManagerSaveException;
    void updateEpic(Epic updateEpic) throws ManagerSaveException;
    void clearEpicById(int id) throws ManagerSaveException;

    List<Task> getHistory();
}
