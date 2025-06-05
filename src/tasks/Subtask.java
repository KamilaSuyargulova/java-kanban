package tasks;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String taskDescription, int id, int epicId) {
        super(taskName, taskDescription, id);
        this.epicId = epicId;
        this.taskType = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{ taskName: " + getTaskName() +
                ", epicId = " + getEpicId() +
                ", id = " + getId() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                "}";
    }

}
