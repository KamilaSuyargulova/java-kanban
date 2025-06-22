package manager;

import tasks.*;

public class StringConverter {

    public static String taskToString(Task task) {
        String type = TaskType.TASK.toString();
        String epicId = "";

        if (task.getType().equals(TaskType.EPIC)) {
            type = TaskType.EPIC.toString();
        } else if (task.getType().equals(TaskType.SUBTASK)) {
            type = TaskType.SUBTASK.toString();
            epicId = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.join(",",
                String.valueOf(task.getId()),
                type,
                task.getTaskName(),
                task.getTaskStatus().toString(),
                task.getTaskDescription(),
                epicId
        );
    }

    public static Task taskFromString(String value) {
        String[] parts = value.split(",");

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, id);
                task.setTaskStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, id);
                epic.setTaskStatus(status);
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(name, description, id, epicId);
                subtask.setTaskStatus(status);
                return subtask;
        }
        return null;
    }

}
