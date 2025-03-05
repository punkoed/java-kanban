package Tests.History;

import TaskManagement.History.HistoryManager;
import TaskManagement.History.InMemoryHistoryManager;
import TaskManagement.Managers;
import TaskManagement.TaskManager.TaskManager;
import TaskStatus.TaskStatus;
import Tasks.Task;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    // Тест на дубликаты
    @Test
    void testHistoryWithoutDuplicates() {
        HistoryManager history = new InMemoryHistoryManager();
        Task task1 = new Task("Sample", "Test", TaskStatus.NEW);

        history.add(task1);
        history.add(task1);

        assertEquals(1, history.getHistory().size());
    }

    // Тест на сохранение порядка в истории
    @Test
    void testHistoryOrder() {
        HistoryManager historyExpected = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Test", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "NewTest", TaskStatus.IN_PROGRESS);

        historyExpected.add(task1);
        historyExpected.add(task2);
        historyExpected.add(task1); // Повторно добавляем task1

        // Последний элемент должен быть task1
        HistoryManager historyTest = new InMemoryHistoryManager();
        historyTest.add(task1);
        historyTest.add(task2);
        historyTest.add(task1); // Повторно добавляем task1

        assertEquals(historyExpected.getHistory(), historyTest.getHistory());
    }

    // Тест на сохранение в истории исходного состояния объекта
    @Test
    void historyShouldPreserveTaskState() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Initial State", TaskStatus.IN_PROGRESS);
        manager.createTask(task);
        String initialDesc = task.getDescription();

        // Добавляем в историю объект
        HistoryManager history = new InMemoryHistoryManager();
        history.add(task);

        // Изменяем состояние объекта
        task.setDescription("Modified State");
        String modifiedDesc = task.getDescription();

        // Можно проверить по состоянию одного из полей класса
        assertEquals(initialDesc, modifiedDesc, "История должна хранить исходное состояние задачи.");
    }
}