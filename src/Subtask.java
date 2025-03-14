public class Subtask extends Task {
    int epicCode;

    public Subtask(String taskName, String taskDescription, int taskCode, TaskStatus taskStatus, int epicCode) {
        super(taskName, taskDescription, taskCode, taskStatus);
        this.epicCode = epicCode;
    }

    public int getEpicCode() {
        return epicCode;
    }

    @Override
    public String toString() {
        return "Subtask{ taskName: " + getTaskName() +
                ", epicCode = " + getEpicCode() +
                ", taskCode = " + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                "}";
    }

}
