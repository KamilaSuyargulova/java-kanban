import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int taskCode = 1;

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getAllSubtasksByEpicCode(int taskCode) {
        Epic epic = epics.get(taskCode);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return new ArrayList<>();
    }

    public void addNewTask(Task task) {
        task.setTaskCode(taskCode++);
        tasks.put(task.getTaskCode(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setTaskCode(taskCode++);
        epics.put(epic.getTaskCode(), epic);
    }

    public void addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicCode());
        if (epic != null) {
            subtask.setTaskCode(taskCode++);
            subtasks.put(subtask.getTaskCode(), subtask);
            epic.addSubtask(subtask);
            epic.epicStatusTracker();
        }
    }

    public Task getTaskByCode(int taskCode) {
        return tasks.get(taskCode);
    }

    public Epic getEpicByCode(int taskCode) {
        return epics.get(taskCode);
    }

    public Subtask getSubtaskByCode(int taskCode) {
        return subtasks.get(taskCode);
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatusTracker();
        }
    }

    public void removeTaskByCode(int taskCode) {
        tasks.remove(taskCode);
    }

    public void removeEpicByCode(int taskCode) {
        Epic epicToRemove = epics.get(taskCode);
        if (epicToRemove.getSubtasks() != null) {
            for (Subtask subtask : epicToRemove.getSubtasks()) {
                subtasks.remove(subtask.getTaskCode());
            }
            epics.remove(taskCode);
        }
    }

    public void removeSubtaskByCode(int taskCode) {
        Subtask subtaskToRemove = subtasks.get(taskCode);
        Epic subtasksEpic = epics.get(subtaskToRemove.getEpicCode());
        subtasksEpic.removeSubtask(subtaskToRemove);
        subtasks.remove(taskCode);
        subtasksEpic.epicStatusTracker();
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskCode(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskCode(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskCode(), subtask);
        Epic epic = epics.get(subtask.getEpicCode());
        if (epic != null) {
            epic.epicStatusTracker();
        }
    }

}






