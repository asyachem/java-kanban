import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Task;
import tasks.Subtask;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("task1","описание", 20, LocalDateTime.of(2024, 6, 10, 10, 0));
        Task task2 = new Task("task2","описание", 20, LocalDateTime.of(2024, 6, 1, 10, 0));
        Task task3 = new Task("task3","описание", 20, LocalDateTime.of(2024, 6, 5, 15, 0));
        Task task4 = new Task("task4","описание", 20, LocalDateTime.of(2024, 6, 1, 9, 30));
        Task task5 = new Task("task5","описание", 10, LocalDateTime.of(2024, 6, 1, 9, 40));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.addTask(task5);

        Epic epic = new Epic("Глобальная уборка", "на выходных");
        manager.addEpic(epic);
        Subtask sub1 = new Subtask("Мыть полы", "выходные", epic.getId(), 20, LocalDateTime.of(2024, 7, 10, 10, 0));
        Subtask sub2 = new Subtask("Пропылесосить", "выходные", epic.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);

        Epic epic2 = new Epic("Переезд", "переезд на дачу");
        manager.addEpic(epic2);
        Subtask sub3 = new Subtask("Морально подготовиться", "когда-нибудь", epic2.getId());
        manager.addSubtask(sub3);

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
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
