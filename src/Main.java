import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Стирка","стираем белье");
        Task task2 = new Task("Глажка","гладим белье");

        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic = new Epic("Глобальная уборка", "на выходных");
        manager.addEpic(epic);
        Subtask sub1 = new Subtask("Мыть полы", "выходные", epic.getId());
        Subtask sub2 = new Subtask("Пропылесосить", "выходные", epic.getId());
        manager.addSubtask(sub1, epic);
        manager.addSubtask(sub2, epic);

        Epic epic2 = new Epic("Переезд", "переезд на дачу");
        manager.addEpic(epic2);
        Subtask sub3 = new Subtask("Морально подготовиться", "когда-нибудь", epic2.getId());
        manager.addSubtask(sub3, epic2);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getListSubtasksofEpic(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
    }
}
