import TaskManagement.*;
import TaskManagement.TaskManager.*;
import TaskStatus.TaskStatus;
import Tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создание обычной задачи
        Task task1 = new Task("Переезд", "Переезд в новую квартиру", TaskStatus.NEW);
        manager.createTask(task1);

        // Создание эпика
        EpicTask epic1 = new EpicTask("Ремонт", "Капитальный ремонт квартиры");
        manager.createEpic(epic1);

        // Создание подзадач
        Subtask subtask1 = new Subtask("Покраска стен", "Выбрать цвет и покрасить", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Укладка плитки", "Укладка новой плитки", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask2);

        // Просмотр задач
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        // Печать истории
        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("История просмотров:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}