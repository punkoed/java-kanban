package Tasks;

import TaskStatus.TaskStatus;
import java.util.ArrayList;

public final class EpicTask extends Task {

    private final ArrayList<Integer> subTaskIds = new ArrayList<>();

    public EpicTask(String title, String description) {
        super(title, description);

        this.status = TaskStatus.NEW;

    }

    // геттеры и сеттеры

    public ArrayList<Integer> getSubtaskIds() {
        return subTaskIds;
    }

    // взаимодействия со списком id подзадач
    public void addSubTaskId(final int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void removeSubTaskId(final int subTaskId) {
        subTaskIds.remove(subTaskId);
    }

    // Override
    @Override
    public String toString() {
        return "EpicTask [id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", subTaskIds=" + subTaskIds + "]";
    }
}


