package Tasks;

import TaskStatus.TaskStatus;
import java.util.Objects;

public class Task {

    protected int id;
    protected String title;
    protected String description;
    protected TaskStatus status;

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;

    }

    // геттеры и сеттеры

    public int getId() {
        return id;
    }
    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    // Override
    @Override
    public final boolean equals(final Object anyTask) {
        if (this == anyTask) return true;
        if ((anyTask == null) || (getClass() != anyTask.getClass())) return false;
        Task task = (Task) anyTask;
        return Objects.equals(title, task.title) &&
                Objects.equals(id, task.id) &&
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public final int hashCode() {
        int hash = 17;

        if (title != null) hash = hash + title.hashCode();
        if (description != null) hash = hash + description.hashCode();
        if (id != 0) hash = hash + Integer.hashCode(id);
        if (status != null) hash = hash + status.hashCode();

        hash = hash * 31;
        return hash;
    }

    @Override
    public String toString() {
        return "Task [id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", status=" + status + "]";
    }
}
