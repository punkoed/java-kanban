package TaskManagement.History;


import Tasks.Task;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    private static final byte MAX_HISTORY_SIZE = 10;

    @Override
    public void add(final Task task) {
        if (task == null) return;

        history.remove(task); // убираем дубликат
        // используем конструктор для копирования
        Task newTask = new Task(task);
        history.addLast(newTask);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return new LinkedList<>(history);
    }
}