package manager;

import history.HistoryManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subTasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected Comparator<Task> taskComparator = new TaskComparator();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);
    private int numberId = 0;

// методы по Task
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearAllTasks() {
        for (Task task : tasks.values()){
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }

        tasks.clear();
        System.out.println("tasks.Task пустой");
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);

        return task;
    }

    @Override
    public void addTask(Task newTask) {
        if (newTask.getDuration() != null) {
            try {
                checkTaskTime(newTask);
            } catch (PrioritizedTaskSaveException e) {
                throw new PrioritizedTaskSaveException(e.getMessage());
            }
            this.prioritizedTasks.add(newTask);
        }

        newTask.setId(generateId());
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void updateTask(Task updateTask) {
        if (updateTask.getDuration() != null) {
            try {
                checkTaskTime(updateTask);
            } catch (PrioritizedTaskSaveException e) {
                throw new PrioritizedTaskSaveException(e.getMessage());
            }
            this.prioritizedTasks.remove(this.getTaskById(updateTask.getId()));
            this.prioritizedTasks.add(updateTask);
        }

        if (tasks.containsKey(updateTask.getId())) {
            System.out.println("Задача найдена. Обновляем.");
            tasks.put(updateTask.getId(), updateTask);

            System.out.println("Задачу обновили.");
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void clearTaskById(int id) {
        Task task = this.getTaskById(id);
        if (this.prioritizedTasks.contains(task)) {
            this.prioritizedTasks.remove(task);
        }

        if (tasks.containsKey(id)) {
            tasks.remove(id);

            historyManager.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

//  методы для Subtask
    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getDuration() != null) {
            try {
                checkTaskTime(subtask);
            } catch (PrioritizedTaskSaveException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.add(subtask);
        }

        subtask.setId(generateId());
        subTasks.put(subtask.getId(), subtask);

        Epic epic = getEpicById(subtask.getIdEpic());

        epic.getSubtasks().add(subtask.getId());

        // обновляем статусы сабтасков
        changeStatusOfEpic(epic);
        // расчет длительности, начало и конец эпика
        calculateEpicTime(epic);
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllSubtasks() {
        for (Subtask subtask : subTasks.values()){
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        }

        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = subTasks.get(id);
        historyManager.add(subtask);

        return subtask;
    }

    @Override
    public List<Subtask> getListSubtasksofEpic(Epic epic) {
        List<Subtask> listSubtaskOfThisEpic = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasks()) {
            listSubtaskOfThisEpic.add(subTasks.get(subtaskId));
        }

        return listSubtaskOfThisEpic;
    }

    @Override
    public void updateSubtask(Subtask updateTask) {
        if (updateTask.getDuration() != null) {
            try {
                checkTaskTime(updateTask);
            } catch (PrioritizedTaskSaveException e) {
                System.out.println(e.getMessage());
                return;
            }
            this.prioritizedTasks.remove(this.getSubTaskById(updateTask.getId()));
            this.prioritizedTasks.add(updateTask);
        }

        if (subTasks.containsKey(updateTask.getId())) {
            System.out.println("Сабтаск найден. Обновляем.");
            subTasks.put(updateTask.getId(), updateTask);

            Epic thisEpic = getEpicById(updateTask.getIdEpic());

            // обновляем статусы сабтасков
            changeStatusOfEpic(thisEpic);
            // расчет длительности, начало и конец эпика
            calculateEpicTime(thisEpic);
            System.out.println("Сабтаск обновили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

    @Override
    public void clearSubtaskById(Integer id) {
        if (this.prioritizedTasks.contains(this.getSubTaskById(id))) {
            this.prioritizedTasks.remove(this.getSubTaskById(id));
        }

        if (subTasks.containsKey(id)) {
            Subtask subtask = getSubTaskById(id);
            int epicId = subtask.getIdEpic();
            Epic epic = getEpicById(epicId);
            epic.getSubtasks().remove(id);
            subTasks.remove(id);

            historyManager.remove(id);

            // обновляем статус эпика
            changeStatusOfEpic(epic);
            // расчет длительности, начало и конец эпика
            calculateEpicTime(epic);

            System.out.println("Сабтаск удалили.");
        } else {
            System.out.println("Такого сабтаска нет");
        }
    }

// методы для Epic
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearAllEpics() {
        for (int id : epics.keySet()){
            historyManager.remove(id);
        }

        epics.clear();
        System.out.println(epics);
        System.out.println("Эпики пустые");

        for (int id : subTasks.keySet()){
            historyManager.remove(id);
        }

        subTasks.clear();
        System.out.println(subTasks);
        System.out.println("Сабтаски пустые");
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void addEpic(Epic newEpic) {
        newEpic.setId(generateId());
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            System.out.println("Эпик найден. Обновляем.");
            epics.put(updateEpic.getId(), updateEpic);
            System.out.println("Эпик обновили.");
            // обновляем статус эпика
            changeStatusOfEpic(updateEpic);
            // расчет длительности, начало и конец эпика
            calculateEpicTime(updateEpic);
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    @Override
    public void clearEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = getEpicById(id);
            // удалили у эпика его сабтаски из общего списка сабтасков
            for (Integer subtaskId : epic.getSubtasks()) {
                subTasks.remove(subtaskId);

                historyManager.remove(subtaskId);
            }
            // удалили сам эпик из общего списка
            epics.remove(id);

            historyManager.remove(id);

            System.out.println("Эпик удалили.");
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    //прочие методы
    private int generateId() {
        return numberId++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return new TreeSet<>(this.prioritizedTasks);
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

    // методы по расчету времени тасков, сабтасков и эпиков
    public void calculateEpicTime (Epic epic) {
        this.calculateEpicDuration(epic);
        this.getEpicStartTime(epic);
        this.getEpicEndTime(epic);
    }

    private void calculateEpicDuration (Epic epic) {
        Duration currentDuretion = epic.getDuration();
        List<Subtask> listSubtasks = this.getListSubtasksofEpic(epic);

        for (Subtask s : listSubtasks) {
            if (s.getDuration() != null && currentDuretion == null) {
                currentDuretion = s.getDuration();
            }
            if (s.getDuration() != null && currentDuretion != null) {
                currentDuretion.plusMinutes(s.getDuration().toMinutes());
            }
        }
        epic.setDuration(currentDuretion);
    }

    private LocalDateTime getEpicStartTime(Epic epic) {
        List<Subtask> listSubtasks = this.getListSubtasksofEpic(epic);
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Subtask s : listSubtasks) {
            if (s.getStartTime() != null && s.getStartTime().isBefore(startTime)) {
                startTime = s.getStartTime();
            }
        }
        if (startTime == LocalDateTime.MAX) startTime = null;
        epic.setStartTime(startTime);
        return startTime;
    }

    private void getEpicEndTime(Epic epic) {
        LocalDateTime startTime = this.getEpicStartTime(epic);
        List<Subtask> listSubtasks = this.getListSubtasksofEpic(epic);
        for (Subtask s : listSubtasks) {
            if(s.getDuration() != null) {
                startTime.plus(s.getDuration());
            }
        }
    }

    @Override
    public void checkTaskTime(Task task) {
        TreeSet<Task> prioritizedT = this.prioritizedTasks;
        for (Task task1 : prioritizedT) {
            if ((task1.getStartTime().equals(task.getStartTime()) && task1.getEndTime().equals(task.getEndTime())) || (task1.getStartTime().isBefore(task.getEndTime()) && task1.getEndTime().isAfter(task.getStartTime()))) {
                throw new PrioritizedTaskSaveException("Уже есть задача с таким временем");
            }
        }
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
