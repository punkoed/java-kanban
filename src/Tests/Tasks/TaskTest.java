package Tests.Tasks;

import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    // Тест равенства задач по id
    @Test
    void tasksWithSameIdShouldBeEqual() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task 1", "Description", TaskStatus.NEW);
        manager.createTask(task1);
        int id = task1.getId();

        task1 = new Task("Task Modified", "Another Description", TaskStatus.NEW);
        manager.updateTask(task1);
        int testId = task1.getId();

        assertEquals(manager.getTaskById(id), manager.getTaskById(testId), "Задачи с одинаковым id должны быть равны.");
    }

    // Тест неизменности задачи после добавления
    @Test
    void taskShouldNotChangeAfterAddition() {
        TaskManager manager = Managers.getDefault();

        Task expected = new Task("Original", "Desc", TaskStatus.NEW);
        Task addedTask = new Task(expected.getTitle(), expected.getDescription(), expected.getStatus());
        manager.createTask(addedTask);

        assertEquals(expected, addedTask, "Задача не должна изменяться.");
    }

}