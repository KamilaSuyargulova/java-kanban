package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskName, String taskDescription, int taskCode) {
        super(taskName, taskDescription, taskCode);
        this.subtasks = new ArrayList<>();
    }

    public void epicStatusTracker() {
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
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "tasks.Epic{ taskName: " + getTaskName() +
                ", taskCode = " + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                ", subtasks: " + subtasks +
                "}";

    }

}
