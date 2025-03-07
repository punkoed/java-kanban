package Tests.TaskManager;

import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.EpicTask;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {

    // Тест добавления задач в менеджер
    @Test
    void shouldAddAndFindTasksById() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        manager.createTask(task);

        EpicTask epic = new EpicTask("Epic", "Desc");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Desc", TaskStatus.NEW, epic.getId());
        manager.createSubtask(subtask);

         assertNotNull(manager.getTaskById(task.getId()));
         assertNotNull(manager.getEpicById(epic.getId()));
         assertNotNull(manager.getSubtaskById(subtask.getId()));
    }
}