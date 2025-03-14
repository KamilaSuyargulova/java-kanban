public class Task {
    private String taskName;
    private String taskDescription;
    private int taskCode;
    private TaskStatus taskStatus;


    Task(String taskName, String taskDescription, int taskCode, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskCode = taskCode;
        this.taskStatus = taskStatus;
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
    public String toString() {
        return "Task{ taskName: " + getTaskName() +
                ", taskCode =" + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() + "}";
    }

}
