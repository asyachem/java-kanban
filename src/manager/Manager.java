package manager;

import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    private int numberId = 0;

// методы по Task
    public ArrayList<Task> showAllTasks () {
        return new ArrayList<>(tasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();
        System.out.println(tasks);
        System.out.println("tasks.Task пустой");
    }

    public Task showTaskById(int id) {
        return tasks.get(id);
    }

    public void addTask(Task newTask) {
        newTask.setId(generateId());
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }

    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            System.out.println("Задача найдена. Обновляем.");
            tasks.put(updateTask.getId(), updateTask);

            System.out.println("Задачу обновили.");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    public void clearTaskById (int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

            System.out.println("Задачу удалили.");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

//  методы для Subtask

    //добавление сабтаска в эпик и в список всех сабтасков
    public void addSubtask(Subtask subtask, Epic epic){
        subtask.setId(generateId());
        subTasks.put(subtask.getId(), subtask);

        epic.getSubtasks().add(subtask.getId());

        // обновляем статусы сабтасков
        changeStatusOfEpic(epic);

        System.out.println("Сабтаск добавлен");
    }

    // показывает список всех сабтасков всего
    public ArrayList<Subtask> showAllSubtasks () {
        return new ArrayList<>(subTasks.values());
    }

    // удаление всех сабтасков
    public void clearAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    // получение по айди
    public Subtask showSubTaskById(int id) {
        return subTasks.get(id);
    }

    // обновить сабтаск
    public void updateSubtask(Subtask updateTask) {
        if (subTasks.containsKey(updateTask.getId())) {
            System.out.println("Сабтаск найден. Обновляем.");
            subTasks.put(updateTask.getId(), updateTask);

            Epic thisEpic = showEpicById(updateTask.getIdEpic());

            // обновляем статусы сабтасков
            changeStatusOfEpic(thisEpic);

            System.out.println("Сабтаск обновили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

    //удалить сабтаск по айди
    public void clearSubtaskById (Integer id) {
        if (subTasks.containsKey(id)) {
            Subtask subtask = showSubTaskById(id);
            int epicId = subtask.getIdEpic();
            Epic epic = showEpicById(epicId);
            epic.getSubtasks().remove(id);
            subTasks.remove(id);

            // обновляем статус эпика
            changeStatusOfEpic(epic);

            System.out.println("Сабтаск удалили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

// -------------------- методы для tasks.Epic ----------------------

    public ArrayList<Epic> showAllEpics () {
        return new ArrayList<>(epics.values());
    }

    public void clearAllEpics() {
        epics.clear();
        System.out.println(epics);
        System.out.println("Эпики пустые");
        subTasks.clear();
        System.out.println(subTasks);
        System.out.println("Сабтаски пустые");
    }

    public Epic showEpicById(int id) {
        return epics.get(id);
    }

    public void addEpic(Epic newEpic) {
        newEpic.setId(generateId());
        epics.put(newEpic.getId(), newEpic);
        System.out.println("Эпик добавлен");
    }

    public void updateEpic (Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            System.out.println("Эпик найден. Обновляем.");
            epics.put(updateEpic.getId(), updateEpic);
            System.out.println("Эпик обновили.");
            // обновляем статус эпика
            changeStatusOfEpic(updateEpic);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    public void clearEpicById (int id) {
        if (epics.containsKey(id)) {
            Epic epic = showEpicById(id);
            // удалили у эпика его сабтаски из общего списка сабтасков
            for (Integer subtaskId : epic.getSubtasks()) {
                subTasks.remove(subtaskId);
            }
            // удалили сам эпик из общего списка
            epics.remove(id);

            System.out.println("Эпик удалили.");
        } else {
            System.out.println("Такого эпика нет");
        }
    }

//
    private int generateId() {
        return numberId++;
    }

    public List<Subtask> getListSubtasksofEpic(Epic epic) {
        List<Subtask> listSubtaskOfThisEpic = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasks()) {
            listSubtaskOfThisEpic.add(subTasks.get(subtaskId));
        }

        System.out.println(listSubtaskOfThisEpic);
        return listSubtaskOfThisEpic;
    }

    private void changeStatusOfEpic (Epic epic) {

        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int numberOfDone = 0;
        int numberOfNew = 0;
        List<Subtask> updatedSubtask = getListSubtasksofEpic(epic);

        for (int i=0; i < updatedSubtask.size(); i++) {
            if (updatedSubtask.get(i).getStatus() == Status.DONE) {
                numberOfDone++;
            } else if (updatedSubtask.get(i).getStatus() == Status.NEW) {
                numberOfNew++;
            }
        }

        if (numberOfDone == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else if (numberOfNew == epic.getSubtasks().size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

}
