import manager.Manager;
import status.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

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

    }
}
