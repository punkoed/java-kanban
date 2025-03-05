package TaskManagement.TaskManager;

import TaskManagement.History.HistoryManager;
import TaskManagement.Managers;
import TaskStatus.TaskStatus;
import Tasks.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 0;
    private final static byte MAX_HISTORY_SIZE = 10;

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, EpicTask> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;

    // Конструктор
    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    // Методы для работы с историей
    public void addToHistory(final Task task) {
        if (task == null) return;

        LinkedList<Task> history = historyManager.getHistory();
        history.remove(task); // убираем дубликат
        // Если передать просто task, то history будет хранить ссылку на него => при изменении состояния task
        // изменится и его состояние в history, а такого быть не должно. History должна хранить то состояние,
        // которое в него изначально передали. Для этого создаю новую переменную.
        Task newTask = new Task(task.getTitle(), task.getDescription(), task.getStatus());

        history.addLast(newTask);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }
    public LinkedList<Task> getHistory() {
        return historyManager.getHistory();
    }

    //------------------------------------------------------

    // Метод для генерации id
    private int generateId() {
        return nextId++;
    }

    //------------------------------------------------------

    // Методы для обычных задач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(final int id) {
        Task targetTask = tasks.get(id);
        addToHistory(targetTask);
        return targetTask;
    }

    @Override
    public void createTask(final Task task) {
        if (task == null) return;

        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }
    @Override
    public void updateTask(final Task updTask) {
        int id = updTask.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, updTask);
        }
    }

    @Override
    public void deleteTaskById(final int id) {
        tasks.remove(id);
    }

    //------------------------------------------------------

    // Методы для эпиков
    @Override
    public ArrayList<EpicTask> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (EpicTask epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    @Override
    public EpicTask getEpicById(final int id) {
        EpicTask targetEpic = epics.get(id);
        addToHistory(targetEpic);
        return targetEpic;
    }

    @Override
    public void createEpic(final EpicTask epic) {
        if (epic == null) return;

        int id = generateId();
        epic.setId(id);
        epic.clearSubTaskIds();
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(final EpicTask updEpic) {
        int id = updEpic.getId();
        if (epics.containsKey(id)) {
            EpicTask oldEpic = epics.get(id);
            oldEpic.setTitle(updEpic.getTitle());
            oldEpic.setDescription((updEpic.getDescription()));
        }
    }

    @Override
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
    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
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

    @Override
    public Subtask getSubtaskById(final int id) {
        Subtask targetSubtask = subtasks.get(id);
        addToHistory(targetSubtask);
        return targetSubtask;
    }

    @Override
    public void createSubtask(final Subtask subtask) {
        if (subtask == null) return;

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

    @Override
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

    @Override
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

    @Override
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

    //------------------------------------------------------

    //Метод для проверки
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);
            for (Task task : manager.getSubtasksOfEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}