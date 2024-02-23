public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Стирка","стираем белье");
        Task task2 = new Task("Глажка","гладим белье");

        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic = new Epic("Глобальная уборка", "на выходных");
        Subtask sub1 = new Subtask("Мыть полы", "выходные");
        Subtask sub2 = new Subtask("Пропылесосить", "выходные");

        manager.addEpic(epic);
        manager.addSubtask(sub1, epic);
        manager.addSubtask(sub2, epic);

        Epic epic2 = new Epic("Переезд", "переезд на дачу");
        Subtask sub3 = new Subtask("Морально подготовиться", "когда-нибудь");

        manager.addEpic(epic2);
        manager.addSubtask(sub3, epic2);

        System.out.println("--------");
// вывели все таски, сабтаски и эпики
        System.out.println(task1);
        System.out.println(task2);

        System.out.println(sub1);
        System.out.println(sub2);
        System.out.println(sub3);

        System.out.println(epic);
        System.out.println(epic2);

        System.out.println("--------");
// обновляем статус тасков и сабтасков
        manager.updateTask(new Task("Оплатить счета","сегодня"), 0, Status.DONE);
        manager.showAllTasks();

        manager.updateSubtask(new Subtask("Собрать одежду", "сегодня"), 6, epic2, Status.DONE);
        manager.showAllEpics();

        System.out.println("--------");
// удаляем один эпик
        manager.deleteEpicById(5);
        manager.showAllEpics();
        manager.showAllSubtasks();

        System.out.println("--------");
// удалили одну задачу
        manager.deleteTaskById(0);
        manager.showAllTasks();
    }
}
