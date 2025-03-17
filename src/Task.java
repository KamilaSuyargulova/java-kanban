public class Task {
    private String taskName;
    private String taskDescription;
    private int taskCode;
    private TaskStatus taskStatus;


    Task(String taskName, String taskDescription, int taskCode) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus.NEW;
        this.taskCode = taskCode;
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
    public String toString() {
        return "Task{ taskName: " + getTaskName() +
                ", taskCode =" + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() + "}";
    }

}
