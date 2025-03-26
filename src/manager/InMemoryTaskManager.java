package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int taskCode = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpicCode(int taskCode) {
        Epic epic = epics.get(taskCode);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return new ArrayList<>();
    }

    @Override
    public int addNewTask(Task task) {
        task.setTaskCode(taskCode++);
        tasks.put(task.getTaskCode(), task);
        return 0;
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setTaskCode(taskCode++);
        epics.put(epic.getTaskCode(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicCode());
        if (epic != null) {
            subtask.setTaskCode(taskCode++);
            subtasks.put(subtask.getTaskCode(), subtask);
            epic.addSubtask(subtask);
            epic.epicStatusTracker();
        }
    }

    @Override
    public Task getTaskByCode(int taskCode) {
        historyManager.add(tasks.get(taskCode));
        return tasks.get(taskCode);
    }

    @Override
    public Epic getEpicByCode(int taskCode) {
        historyManager.add(epics.get(taskCode));
        return epics.get(taskCode);
    }

    @Override
    public Subtask getSubtaskByCode(int taskCode) {
        historyManager.add(subtasks.get(taskCode));
        return subtasks.get(taskCode);
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatusTracker();
        }
    }

    @Override
    public void removeTaskByCode(int taskCode) {
        tasks.remove(taskCode);
    }

    @Override
    public void removeEpicByCode(int taskCode) {
        Epic epicToRemove = epics.get(taskCode);
        if (epicToRemove.getSubtasks() != null) {
            for (Subtask subtask : epicToRemove.getSubtasks()) {
                subtasks.remove(subtask.getTaskCode());
            }
            epics.remove(taskCode);
        }
    }

    @Override
    public void removeSubtaskByCode(int taskCode) {
        Subtask subtaskToRemove = subtasks.get(taskCode);
        Epic subtasksEpic = epics.get(subtaskToRemove.getEpicCode());
        subtasksEpic.removeSubtask(subtaskToRemove);
        subtasks.remove(taskCode);
        subtasksEpic.epicStatusTracker();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskCode(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskCode(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskCode(), subtask);
        Epic epic = epics.get(subtask.getEpicCode());
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            for (int i = 0; i < subtasksInEpic.size(); i++) {
                if (subtasksInEpic.get(i).getTaskCode() == subtask.getTaskCode()) {
                    subtasksInEpic.set(i, subtask);
                    break;
                }
            }
            epic.epicStatusTracker();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

}