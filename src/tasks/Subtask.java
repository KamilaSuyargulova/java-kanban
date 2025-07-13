package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String taskDescription, int id, int epicId, Duration duration,
                   LocalDateTime startTime) {
        super(taskName, taskDescription, id, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{ taskName: " + getTaskName() +
                ", epicId = " + getEpicId() +
                ", id = " + getId() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                ", startTime: " + getStartTime() +
                ", duration: " + getDuration() +
                ", endTime" + getEndTime() + "}";
    }

}
