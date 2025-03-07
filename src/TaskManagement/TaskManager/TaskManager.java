package TaskManagement.TaskManager;

import Tasks.*;
import java.util.List;
import java.util.ArrayList;

public interface TaskManager {

    // Методы для работы с историей

    List<Task> getHistory();

    // Методы для обычных задач
    List<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task updTask);

    void deleteTaskById(int id);

    // Методы для эпиков
    List<EpicTask> getAllEpics();

    void deleteAllEpics();

    EpicTask getEpicById(int id);

    void createEpic(EpicTask epic);

    void updateEpic(EpicTask updEpic);

    void deleteEpicById(int id);

    // Методы для подзадач
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask updSubtask);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksOfEpic(int epicId);
}
