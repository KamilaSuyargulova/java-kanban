package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int id = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(
                            Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Task::getId)
    );

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void validateTaskTime(Task newTask) {
        if (newTask.getStartTime() == null) {
            return;
        }

        prioritizedTasks.stream()
                .filter(t -> isTimeOverlap(t, newTask))
                .findFirst()
                .ifPresent(t -> {
                    throw new ManagerValidateException(
                            "Задача '" + newTask.getTaskName() +
                                    "' пересекается с '" + t.getTaskName() + "'");
                });
    }

    @Override
    public boolean isTimeOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

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
    public void addNewTask(Task task) {
        validateTaskTime(task);
        task.setId(id++);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        validateTaskTime(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(id++);
            subtasks.put(subtask.getId(), subtask);
            epic.addSubtask(subtask);
            epic.updateEpicStatusTime();
            prioritizedTasks.add(subtask);
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
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        subtasks.clear();
        epics.keySet().forEach(historyManager::remove);
        epics.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubtasks().clear();
            epic.updateEpicStatusTime();
        });
    }

    @Override
    public boolean removeTaskById(int id) {
        tasks.remove(id);
        prioritizedTasks.removeIf(t -> t.getId() == id);
        historyManager.remove(id);
        return true;
    }

    @Override
    public boolean removeEpicById(int id) {
        Epic epicToRemove = epics.get(id);
        if (epicToRemove.getSubtasks() != null) {
            for (Subtask subtask : epicToRemove.getSubtasks()) {
                historyManager.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
        prioritizedTasks.removeIf(t -> t.getId() == id);
        historyManager.remove(id);
        return true;
    }

    @Override
    public boolean removeSubtaskById(int id) {
        Subtask subtaskToRemove = subtasks.get(id);
        Epic subtasksEpic = epics.get(subtaskToRemove.getEpicId());
        subtasksEpic.removeSubtask(subtaskToRemove);
        subtasks.remove(id);
        subtasksEpic.updateEpicStatusTime();
        prioritizedTasks.remove(subtaskToRemove);
        historyManager.remove(id);
        return true;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        prioritizedTasks.removeIf(t -> t.getId() == task.getId());
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epic.updateEpicStatusTime();
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
            epic.updateEpicStatusTime();
        }
        prioritizedTasks.add(subtask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}