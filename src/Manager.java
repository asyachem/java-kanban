import java.util.*;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    int numberId = 0;

// методы по Task
    public ArrayList<Task> showAllTasks () {
        ArrayList<Task> listOfTask = new ArrayList<>();

        for (Task task : tasks.values()) {
            listOfTask.add(task);
        }

        System.out.println(listOfTask);
        return listOfTask;
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

        subTasks.put(subtask.getId(), subtask);

        epic.getSubtasks().add(subtask.getId());

        // обновляем статусы сабтасков
        changeStatusOfEpic(epic);

        System.out.println("Сабтаск добавлен");
    }

    // показывает список всех сабтасков всего
    public ArrayList<Subtask> showAllSubtasks () {
        ArrayList<Subtask> listOfSubtask = new ArrayList<>();

        for (Subtask subTask : subTasks.values()) {
            listOfSubtask.add(subTask);
        }

        System.out.println(listOfSubtask);
        return listOfSubtask;
    }


    // удаление всех сабтасков
    public void deleteAllSubtasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    // получение по айди
    public Subtask showSubTaskById(int id) {
        Subtask currentTask = subTasks.get(id);

        System.out.println("Найден нужный сабтаск:");
        System.out.println(currentTask);

        return currentTask;
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
    public void deleteSubtaskById (int id) {
        if (subTasks.containsKey(id)) {
            Subtask subtask = showSubTaskById(id);
            int epicId = subtask.getIdEpic();
            Epic epic = showEpicById(epicId);
            epic.getSubtasks().remove((Integer) id);
            subTasks.remove(id);

            // обновляем статус эпика
            changeStatusOfEpic(epic);

            System.out.println("Сабтаск удалили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

// -------------------- методы для Epic ----------------------

    public ArrayList<Epic> showAllEpics () {
        ArrayList<Epic> listOfEpic = new ArrayList<>();

        for (Epic epic : epics.values()) {
            listOfEpic.add(epic);
        }

        System.out.println(listOfEpic);
        return listOfEpic;
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

    public void deleteEpicById (int id) {
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
        int numberOfDone = 0;
        int numberProgress = 0;
        List<Subtask> updatedSubtask = getListSubtasksofEpic(epic);

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
