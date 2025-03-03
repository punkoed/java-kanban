package TaskManagement;

import TaskStatus.TaskStatus;
import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int nextId = 0;

    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, EpicTask> epics;
    protected final HashMap<Integer, Subtask> subtasks;


    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();

    }

    // Метод для генерации id
    private int generateId() {
        return nextId++;
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

    public void createTask(final Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }
    public void updateTask(final Task updTask) {
        int id = updTask.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, updTask);
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

    public void createEpic(final EpicTask epic) {
        int id = generateId();
        epic.setId(id);
        epic.clearSubTaskIds();
        epics.put(id, epic);
    }

    public void updateEpic(final EpicTask updEpic) {
        int id = updEpic.getId();
        if (epics.containsKey(id)) {
            EpicTask oldEpic = epics.get(id);
            oldEpic.setTitle(updEpic.getTitle());
            oldEpic.setDescription((updEpic.getDescription()));
        }
    }

    public void deleteEpicById(final int id) {
        EpicTask epic = epics.get(id);
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
    private void updateEpicStatus(final EpicTask epic) {
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

    public void createSubtask(final Subtask subtask) {
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

    public void updateSubtask(final Subtask updSubtask) {
        int subId = updSubtask.getId();
        int epicId = updSubtask.getEpicId();
        // Просто проверить существование эпика - мало, так как может быть
        // другой эпик. Нужно проверить, совпадает ли ЭпикИд от этого сабтаска с ЭпикИд
        // сабтаска из списка сабтасков
        if (subtasks.containsKey(subId)
                && getSubtaskById(subId).getEpicId() == epicId) {
            subtasks.put(updSubtask.getId(), updSubtask);
            // Обновление статуса родительского эпика
            EpicTask epic = epics.get(updSubtask.getEpicId());
            updateEpicStatus(epic);
        }
    }

    public void deleteSubtaskById(final int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            EpicTask epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubTaskId(id);
                updateEpicStatus(epic);
            }
        }
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getSubtasksOfEpic(final int epicId) {
        EpicTask epic = epics.get(epicId);
        ArrayList<Subtask> result = new ArrayList<>();
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subId);
                if (subtask != null)
                    result.add(subtask);
            }
        }
        return result;
    }

}