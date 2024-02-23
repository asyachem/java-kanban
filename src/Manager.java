import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();
    //HashMap<Integer, Subtask> epic = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    static int numberId = 0;

// методы по Task
    public void showAllTasks () {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }
    public void clearAllTasks() {
        tasks.clear();
        System.out.println(tasks);
        System.out.println("Task пустой");
    }
    public Task showTaskById(int id) {
        Task currentTask = tasks.get(id);

        System.out.println("Найден нужный Task:");
        System.out.println(currentTask);

        return currentTask;
    }
    public void addTask(Task newTask) {
        newTask.setId(generateId());
        newTask.setStatus(Status.NEW);
        tasks.put(newTask.getId(), newTask);
        System.out.println("Задача добавлена");
    }
    public void updateTask(Task updateTask, int id, Status status) {
        if (tasks.containsKey(id)) {
            System.out.println("Задача найдена. Обновляем.");
            updateTask.setId(id);
            updateTask.setStatus(status);

            tasks.put(id, updateTask);

            System.out.println("Задачу обновили.");
        } else {
            System.out.println("Такой задачи нет");
        }
    }
    public void deleteTaskById (int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);

            System.out.println("Задачу удалили.");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

// -------------------- методы для Subtask ----------------------

    //добавление сабтаска в эпик и в список всех сабтасков
    public void addSubtask(Subtask subtask, Epic epic){
        subtask.setId(generateId());
        subtask.setStatus(Status.NEW);
        subtask.setEpic(epic);

        subTasks.put(subtask.getId(), subtask);

        epic.getSubtasks().add(subtask.getId());

        // обновляем статусы сабтасков
        List<Subtask> updatedSubtask = getListSubtasksofEpic(epic);
        changeStatusOfEpic(updatedSubtask, epic);

        System.out.println("Сабтаск добавлен");
    }

    // показывает список всех сабтасков всего
    public void showAllSubtasks () {
        for (Subtask subTask : subTasks.values()) {
            System.out.println(subTask);
        }
    }

    //показывает список всех сабтасков конкретного эпика
    public void showAllSubtasksofEpic (Epic epic) {
        List<Integer> idSubTasksOfEpic = epic.getSubtasks();

        for (int i=0; i < idSubTasksOfEpic.size(); i++) {
            for (Integer subId : subTasks.keySet()) {
                if (Objects.equals(idSubTasksOfEpic.get(i), subId)) {
                    System.out.println(subTasks.get(subId));
                }
            }
        }

    }

    // удаление всех задач конкретного эпика
    public void deleteAllSubtasks(Epic epic) {
        List<Integer> idSubTasksOfEpic = epic.getSubtasks();

        // удалили сабтаски в общем списке всех сабтасков
        for (int i=0; i < idSubTasksOfEpic.size(); i++) {
            for (Integer subId : subTasks.keySet()) {
                if (Objects.equals(idSubTasksOfEpic.get(i), subId)) {
                    subTasks.remove(subId);
                }
            }
        }

        // удалили все сабтаски в нужном эпике
        epic.getSubtasks().clear();
        //присвоен статус новый
        epic.setStatus(Status.NEW);
    }

    // получение по айди
    public Subtask showSubTaskById(int id) {
        Subtask currentTask = subTasks.get(id);

        System.out.println("Найден нужный сабтаск:");
        System.out.println(currentTask);

        return currentTask;
    }

    // обновить сабтаск
    public void updateSubtask(Subtask updateTask, int id, Epic epic, Status status) {
        if (subTasks.containsKey(id)) {
            System.out.println("Сабтаск найден. Обновляем.");
            updateTask.setId(id);
            updateTask.setStatus(status);

            subTasks.put(id, updateTask);

            // обновляем статусы сабтасков
            List<Subtask> updatedSubtask = getListSubtasksofEpic(epic);
            changeStatusOfEpic(updatedSubtask, epic);

            System.out.println("Сабтаск обновили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

    //удалить сабтаск по айди
    public void deleteSubtaskById (int id, Epic epic) {
        if (subTasks.containsKey(id)) {
            subTasks.remove(id);

            epic.getSubtasks().remove(id);

            // обновляем статусы сабтасков
            List<Subtask> updatedSubtask = getListSubtasksofEpic(epic);
            changeStatusOfEpic(updatedSubtask, epic);

            System.out.println("Сабтаск удалили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

// -------------------- методы для Epic ----------------------

    public void showAllEpics () {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }
    public void clearAllEpics() {
        epics.clear();
        System.out.println(epics);
        System.out.println("Эпики пустые");
    }
    public Epic showEpicById(int id) {
        Epic currentTask = epics.get(id);

        System.out.println("Найден нужный эпик:");
        System.out.println(currentTask);

        return currentTask;
    }
    public void addEpic(Epic newEpic) {
        newEpic.setId(generateId());
        newEpic.setStatus(Status.NEW);
        epics.put(newEpic.getId(), newEpic);
        System.out.println("Эпик добавлен");
    }
    public void updateEpic (Epic updateEpic, int id, Status status) {
        if (epics.containsKey(id)) {
            System.out.println("Эпик найден. Обновляем.");
            updateEpic.setId(id);
            updateEpic.setStatus(status);

            epics.put(id, updateEpic);

            System.out.println("Эпик обновили.");

            // обновляем статусы сабтасков
            List<Subtask> updatedSubtask = getListSubtasksofEpic(updateEpic);
            for (int i=0; i < updatedSubtask.size(); i++) {
                updatedSubtask.get(i).setStatus(Status.NEW);
            }

        } else {
            System.out.println("Такого эпика нет");
        }
    }
    public void deleteEpicById (int id) {
        if (epics.containsKey(id)) {
            // удалили у эпика его сабтаски из общего списка сабтасков
            deleteAllSubtasks(epics.get(id));
            // удалили сам эпик из общего списка
            epics.remove(id);

            System.out.println("Эпик удалили.");
        } else {
            System.out.println("Такого эпика нет");
        }
    }


//
    static int generateId() {
        return numberId++;
    }

    public List<Subtask> getListSubtasksofEpic(Epic epic) {
        List<Integer> idSubTasksOfEpic = epic.getSubtasks();
        List<Subtask> updatedSubtask = new ArrayList<>();
        for (int i=0; i < idSubTasksOfEpic.size(); i++) {
            for (Integer subId : subTasks.keySet()) {
                if (Objects.equals(idSubTasksOfEpic.get(i), subId)) {
                    updatedSubtask.add(subTasks.get(subId));
                }
            }
        }

        return updatedSubtask;
    }

    public void changeStatusOfEpic (List<Subtask> updatedSubtask, Epic epic) {
        int numberOfDone = 0;
        int numberProgress = 0;
        for (int i=0; i < updatedSubtask.size(); i++) {
            if (updatedSubtask.get(i).getStatus() == Status.DONE) {
                numberOfDone++;
            } else if (updatedSubtask.get(i).getStatus() == Status.IN_PROGRESS) {
                numberProgress++;
            }
        }
        if (numberOfDone == 0 && numberProgress == 0) {
            epic.setStatus(Status.NEW);
        } else if (numberOfDone != 0 && numberOfDone == epic.getSubtasks().size()) {
            epic.setStatus(Status.DONE);
        } else if (numberOfDone != 0 && numberOfDone != epic.getSubtasks().size()) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (numberProgress != 0) {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
