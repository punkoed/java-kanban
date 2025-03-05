package Tests.Tasks;

import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.EpicTask;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EpicTaskTest {

    @Test
    void epicTasksWithSameIdShouldBeEqual() {
        TaskManager manager = Managers.getDefault();
        EpicTask epic1 = new EpicTask("Epic 1", "Description");
        manager.createEpic(epic1);
        int id = epic1.getId();

        epic1 = new EpicTask("Epic 1 Modified", "Another Description");
        manager.updateTask(epic1);
        int testId = epic1.getId();

        assertEquals(manager.getTaskById(id), manager.getTaskById(testId), "Эпики с одинаковым id должны быть равны.");
    }

}