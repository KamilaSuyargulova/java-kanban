import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskName, String taskDescription, int taskCode, TaskStatus taskStatus) {
        super(taskName, taskDescription, taskCode, taskStatus);
        this.subtasks = new ArrayList<>();
    }

    public void epicStatusTracker() {
        int counterDone = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
                setTaskStatus(TaskStatus.IN_PROGRESS);
            } else if (subtask.getTaskStatus().equals(TaskStatus.DONE)) {
                counterDone++;
            }
        }
        if (counterDone == subtasks.size()) {
            setTaskStatus(TaskStatus.DONE);
        } else if (counterDone >0) {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }


    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(int isTaskCode) {
        for (Subtask subtask : subtasks) {
            if (isTaskCode == subtask.getTaskCode()) {
                subtasks.remove(subtask);
            }
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        return "Epic{ taskName: " + getTaskName() +
                ", taskCode = " + getTaskCode() +
                ", description: " + getTaskDescription() +
                ", taskStatus: " + getTaskStatus() +
                ", subtasks: " + subtasks +
                "}";

    }

}
