package Tests.Tasks;

import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.EpicTask;
import Tasks.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    // Тест равенства подзадач по id
    @Test
    void subtasksWithSameIdShouldBeEqual() {
        TaskManager manager = Managers.getDefault();

        EpicTask epic1 = new EpicTask("Epic 1", "Desc");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Desc", TaskStatus.NEW, epic1.getId());
        manager.createTask(subtask1);
        int id = subtask1.getId();

        subtask1 = new Subtask("Subtask 1 Modified", "Another Desc", TaskStatus.NEW, epic1.getId());
        manager.updateTask(subtask1);
        int testId = subtask1.getId();

        assertEquals(manager.getSubtaskById(id), manager.getSubtaskById(testId), "Подзадачи с одинаковым id должны быть равны.");
    }
}
