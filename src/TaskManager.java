import java.util.ArrayList;
import java.util.HashMap;

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

    public void addSubtask(String taskName, String taskDescription, int epicCode) {
        for (Epic epic : epics) {
            if (epic.getTaskCode() == epicCode) {
                Subtask subtask = new Subtask(taskName, taskDescription, taskCode, TaskStatus.NEW, epicCode);
                epic.addSubtask(subtask);
                System.out.println("Подзадача добавлена в эпик :" + epic.getTaskName());
            } else {
                System.out.println("Эпик с таким номером не найден");
            }
        }
    }

    public void clearAllTasks() {
        tasks.clear();
        epics.clear();
        taskCode = 1;
        System.out.println("Список задач очищен");
    }

}
