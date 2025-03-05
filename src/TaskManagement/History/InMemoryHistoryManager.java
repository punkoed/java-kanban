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
        // Если передать просто task, то history будет хранить ссылку на него => при изменении состояния task
        // изменится и его состояние в history, а такого быть не должно. History должна хранить то состояние,
        // которое в него изначально передали. Для этого создаю новую переменную.
        Task newTask = new Task(task.getTitle(), task.getDescription(), task.getStatus());
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