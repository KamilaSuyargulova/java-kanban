package tasks;

import java.util.Objects;

public class Task {
    private String taskName;
    private String taskDescription;
    private int taskCode;
    private TaskStatus taskStatus;


    public Task(String taskName, String taskDescription, int taskCode) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.taskCode = taskCode;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskCode(Integer taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getTaskCode() {
        return taskCode;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskCode == task.taskCode;
    }

    @Override
    public String toString() {
        return "tasks.Task{ taskName: " + getTaskName() +
                ", taskCode =" + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() + "}";
    }

}
