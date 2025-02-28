package Tasks;

import TaskStatus.TaskStatus;

public final class Subtask extends Task {

    private final int epicId;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);

        this.epicId = epicId;
    }

    // геттеры и сеттеры

    public int getEpicId() {
        return epicId;
    }

    // Override
    @Override
    public String toString() {
        return "SubTask [id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status +
                ", epicId=" + epicId + "]";
    }
}
