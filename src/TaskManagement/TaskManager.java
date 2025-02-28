package TaskManagement;

import TaskStatus.TaskStatus;
import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

import static TaskManagement.IdGenerator.generateId;

public class TaskManager {
    private static int managerCount = 0;

    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, EpicTask> epics;
    protected final HashMap<Integer, Subtask> subtasks;


    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();

        managerCount++;
    }

    //------------------------------------------------------

    // Методы для обычных задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(final int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(final Task updTask, final TaskStatus updStatus, final int oldTaskId) {
        if (tasks.containsKey(oldTaskId)) {
            Task oldTask = tasks.get(oldTaskId);
            updTask.setStatus(updStatus);
            updTask.setId(oldTaskId);
            tasks.put(updTask.getId(), updTask);
        } else {
            createTask(updTask);
        }
    }

    public void deleteTaskById(final int id) {
        tasks.remove(id);
    }

    //------------------------------------------------------

    // Методы для эпиков
    public ArrayList<EpicTask> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        for (EpicTask epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    public EpicTask getEpicById(final int id) {
        return epics.get(id);
    }

    public void createEpic(EpicTask epic) {
        int id = generateId();
        epic.setId(id);
        epic.getSubtaskIds().clear();
        epics.put(id, epic);
    }

    public void updateEpic(EpicTask updEpic, int oldEpicId) {
        if (epics.containsKey(oldEpicId)) {
            updEpic.setId(oldEpicId);
            epics.put(updEpic.getId(), updEpic);
        } else {
            createEpic(updEpic);
        }
    }

    public void deleteEpicById(final int id) {
        EpicTask epic = getEpicById(id);
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
        }
        epics.remove(id);
     }

    // Обновление статуса эпика:
    // Если подзадач нет или все имеют статус NEW = NEW;
    // Если все подзадачи DONE = DONE;
    // Иначе = IN_PROGRESS.
    private void updateEpicStatus(EpicTask epic) {
        ArrayList<Integer> subIds = epic.getSubtaskIds();
        if (subIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Integer subId : subIds) {
            Subtask subtask = subtasks.get(subId);
            if (subtask != null) {
                if (subtask.getStatus() != TaskStatus.NEW)
                    isAllNew = false;
                if (subtask.getStatus() != TaskStatus.DONE)
                    isAllDone = false;
            }
        }

        if (isAllNew)
            epic.setStatus(TaskStatus.NEW);
        else if (isAllDone)
            epic.setStatus(TaskStatus.DONE);
        else
            epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    //------------------------------------------------------

    // Методы для подзадач
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        // Для каждой подзадачи удаляем её id из родительского эпика и обновляем статус эпика
        for (Subtask subtask : subtasks.values()) {
            EpicTask epic = epics.get(subtask.getEpicId()); // находим родительский эпик
            if (epic != null) {
                epic.removeSubTaskId(subtask.getId());
                updateEpicStatus(epic);
            }
        }
        subtasks.clear();
    }

    public Subtask getSubtaskById(final int id) {
        return subtasks.get(id);
    }

    public void createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        EpicTask epic = epics.get(epicId);
        if (epic != null) {
            int id = generateId();
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.addSubTaskId(id);
            updateEpicStatus(epic);
        }
    }

    public void updateSubtask(final Subtask updSubtask, final TaskStatus status, final int oldSubtaskId) {
        if (subtasks.containsKey(oldSubtaskId)) {
            updSubtask.setStatus(status);
            updSubtask.setId(oldSubtaskId);
            subtasks.put(updSubtask.getId(), updSubtask);
            // Обновление статуса родительского эпика
            EpicTask epic = epics.get(updSubtask.getEpicId());
            if (epic != null)
                updateEpicStatus(epic);
        }
    }

    public void deleteSubtaskById(final int id) {
        Subtask subtask = subtasks.get(id);

        EpicTask epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.removeSubTaskId(id);
            updateEpicStatus(epic);
        }
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksOfEpic(final int epicId) {
        EpicTask epic = epics.get(epicId);
        ArrayList<Subtask> result = new ArrayList<>();
        if (!(epic == null)) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask subtask = getSubtaskById(subId);
                if (subtask != null)
                    result.add(subtask);
            }
        }
        return result;
    }

}