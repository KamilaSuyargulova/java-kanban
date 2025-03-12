import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String taskName, String taskDescription, int taskCode, TaskStatus taskStatus) {
        super(taskName, taskDescription, taskCode, taskStatus);
        this.subtasks = new ArrayList<>();
    }

    //добавить проверку статуса подзадач

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
                ", subtasks: " + subtasks +
                ", taskStatus: " + getTaskStatus() +
                "}";

    }

}
