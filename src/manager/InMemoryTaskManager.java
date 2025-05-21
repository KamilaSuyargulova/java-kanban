package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;
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
    public ArrayList<Subtask> getAllSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return new ArrayList<>();
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return 0;
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(id++);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            epic.epicStatusTracker();
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void clearAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (Subtask subtask : epic.getSubtasks()) {
                historyManager.remove(subtask.getId());
            }
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.epicStatusTracker();
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epicToRemove = epics.get(id);
        if (epicToRemove.getSubtasks() != null) {
            for (Subtask subtask : epicToRemove.getSubtasks()) {
                historyManager.remove(subtask.getId());
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtaskToRemove = subtasks.get(id);
        Epic subtasksEpic = epics.get(subtaskToRemove.getEpicId());
        subtasksEpic.removeSubtask(subtaskToRemove);
        subtasks.remove(id);
        subtasksEpic.epicStatusTracker();
        historyManager.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            for (int i = 0; i < subtasksInEpic.size(); i++) {
                if (subtasksInEpic.get(i).getId() == subtask.getId()) {
                    subtasksInEpic.set(i, subtask);
                    break;
                }
            }
            epic.epicStatusTracker();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}