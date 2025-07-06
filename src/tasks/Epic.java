package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription, int id) {
        super(taskName, taskDescription, id, null, null);
        this.subtasks = new ArrayList<>();
        this.endTime = null;
    }

    public void updateEpicStatusTime() {
        epicStatusTracker();
        calculateTime();
    }

    private void calculateTime() {
        if (subtasks.isEmpty()) {
            setStartTime(null);
            setDuration(null);
            this.endTime = null;
            return;
        }

        LocalDateTime newStartTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(start -> start != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        setStartTime(newStartTime);

        Duration newDuration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, Duration::plus);
        setDuration(newDuration);

        this.endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(end -> end != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private void epicStatusTracker() {
        if (subtasks.isEmpty()) {
            setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean isAllNew = true;
        boolean isAllDone = true;
        for (Subtask subtask : subtasks) {
            if (!subtask.getTaskStatus().equals(TaskStatus.NEW)) {
                isAllNew = false;
            }
            if (!subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                isAllDone = false;
            }
        }
        if (isAllNew) {
            setTaskStatus(TaskStatus.NEW);
        } else if (isAllDone) {
            setTaskStatus(TaskStatus.DONE);
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateEpicStatusTime();
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        updateEpicStatusTime();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "tasks.Epic{ taskName: " + getTaskName() +
                ", id = " + getId() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                ", subtasks: " + subtasks +
                ", startTime: " + getStartTime() +
                ", duration: " + getDuration() +
                ", endTime" + getEndTime() + "}";

    }

}