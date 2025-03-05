package Tests.Managers;

import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    void getDefaultShouldInitialize() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager.getHistory(), "Менеджер должен быть проинициализирован.");
    }
}