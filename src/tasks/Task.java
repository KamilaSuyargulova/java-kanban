package tasks;

import java.util.Objects;

public class Task {
    private String taskName;
    private String taskDescription;
    private int id;
    private TaskStatus taskStatus;
    protected TaskType taskType;


    public Task(String taskName, String taskDescription, int id) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.id = id;
        this.taskType = TaskType.TASK;
    }

    public TaskType getType() {
        return taskType;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public String toString() {
        return "tasks.Task{ taskName: " + getTaskName() +
                ", id =" + getId() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() + "}";
    }

}
