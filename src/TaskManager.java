import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks;
    private ArrayList<Epic> epics;
    private int taskCode;

    TaskManager() {
        tasks = new ArrayList<>();
        epics = new ArrayList<>();
        taskCode = 1;
    }

    public void printAllTasks() {
        if (tasks.isEmpty() && epics.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
            for (Epic epic : epics) {
                System.out.println(epic);
            }
        }
    }

    public void addNewTask(String taskName, String taskDescription) {
        Task task = new Task(taskName, taskDescription, taskCode, TaskStatus.NEW);
        tasks.add(task);
        taskCode++;
    }

    public void addNewEpic(String taskName, String taskDescription) {
        Epic epic = new Epic(taskName, taskDescription, taskCode, TaskStatus.NEW);
        epics.add(epic);
        taskCode++;
    }

    public boolean containsEpic(int epicCode) {
        boolean containsEpic = false;
        for (Epic epic : epics) {
            if (epic.getTaskCode() == epicCode) {
                containsEpic = true;
            }
        }
        return containsEpic;
    }

    public void addSubtask(String taskName, String taskDescription, int epicCode) {
        for (Epic epic : epics) {
            if (epic.getTaskCode() == epicCode) {
                Subtask subtask = new Subtask(taskName, taskDescription, taskCode, TaskStatus.NEW, epicCode);
                epic.addSubtask(subtask);
                System.out.println("Подзадача добавлена в эпик :" + epic.getTaskName());
                taskCode++;
            }
        }
    }

    public void clearAllTasks() {
        tasks.clear();
        epics.clear();
        taskCode = 1;
        System.out.println("Список задач очищен");
    }

    public Task searchByTaskCode(int taskCode) {
        for (Task task : tasks) {
            if (taskCode == task.getTaskCode()) {
                return task;
            }
        }
        for (Epic epic : epics) {
            if (taskCode == epic.getTaskCode()) {
                return epic;
            }
            for (Subtask subtask : epic.getSubtasks()) {
                if (taskCode == subtask.getTaskCode()) {
                    return subtask;
                }
            }
        }
        return null;
    }

    public Task searchByTaskName(String taskName) {
        for (Task task : tasks) {
            if (taskName.equals(task.getTaskName())) {
                return task;
            }
        }
        for (Epic epic : epics) {
            if (taskName.equals(epic.getTaskName())) {
                return epic;
            }
            for (Subtask subtask : epic.getSubtasks()) {
                if (taskName.equals(subtask.getTaskName())) {
                    return subtask;
                }
            }
        }
        return null;
    }

    public void changeStatus(Task task, TaskStatus TaskStatus) {
        task.setTaskStatus(TaskStatus);
        for (Epic epic : epics) {
            epic.epicStatusTracker();
                }
            }


    public void removeTask(int isTaskCode) {
        for (Task task : tasks) {
            if (isTaskCode == task.getTaskCode()) {
                tasks.remove(task);
            }
        }
        for (Epic epic : epics) {
            if (isTaskCode == epic.getTaskCode()) {
                epics.remove(epic);
            }
            epic.removeSubtask(isTaskCode);
        }
    }
}






