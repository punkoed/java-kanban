import TaskManagement.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание обычной задачи
        Task task1 = new Task("Переезд", "Переезд в новую квартиру", TaskStatus.NEW);
        manager.createTask(task1);

        // Создание эпика
        EpicTask epic1 = new EpicTask("Ремонт", "Капитальный ремонт квартиры");
        manager.createEpic(epic1);

        // Создание подзадач
        Subtask subTask1 = new Subtask("Покраска стен", "Выбрать цвет и покрасить", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subTask1);
        Subtask subTask2 = new Subtask("Укладка плитки", "Укладка новой плитки", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subTask2);

        System.out.println("Все обычные задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("\nВсе эпики:");
        for (EpicTask epicTask : manager.getAllEpics()) {
            System.out.println(epicTask);
        }
        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        // Обновляем первую подзадачу

        subTask1.setDescription("Покрасить");
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subTask1);

        System.out.println("\nПосле обновления подзадачи:");
        for (EpicTask epicTask : manager.getAllEpics()) {
            System.out.println(epicTask);
        }

        // Обновляем эпик

        epic1.setTitle("Ремонт!!!!");
        epic1.setDescription("Капитальный ремонт кухни");
        manager.updateEpic(epic1);

        System.out.println("\nПосле обновления эпика:");
        for (EpicTask epicTask : manager.getAllEpics()) {
            System.out.println(epicTask);
        }
    }
}
