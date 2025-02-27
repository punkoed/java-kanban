import TaskManagement.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание обычной задачи
        Task task1 = new Task("Переезд", "Переезд в новую квартиру");
        manager.createTask(task1);

        // Создание эпика
        EpicTask epic1 = new EpicTask("Ремонт", "Капитальный ремонт квартиры");
        manager.createEpic(epic1);

        // Создание подзадач
        Subtask subTask1 = new Subtask("Покраска стен", "Выбрать цвет и покрасить", epic1.getId());
        manager.createSubtask(subTask1);
        Subtask subTask2 = new Subtask("Укладка плитки", "Укладка новой плитки", epic1.getId());
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
        manager.updateSubtask(subTask1, TaskStatus.DONE, subTask2.getId());

        System.out.println("\nВсе подзадачи после апдейта:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nПосле обновления подзадачи:");
        for (EpicTask epicTask : manager.getAllEpics()) {
            System.out.println(epicTask);
        }

        EpicTask epic2 = new EpicTask("Ремонт", "Капитальный ремонт офиса");
        manager.updateEpic(epic2, epic1.getId());

        System.out.println("\nПосле обновления подзадачи:");
        for (EpicTask epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
    }
}
