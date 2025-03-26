package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    //задачи
    ArrayList<Task> getAllTasks();

    int addNewTask(Task task);

    Task getTaskByCode(int taskCode);

    void clearAllTasks();

    void removeTaskByCode(int taskCode);

    void updateTask(Task task);

    //подзадачи
    ArrayList<Subtask> getAllSubtasks();

    void addNewSubtask(Subtask subtask);

    Subtask getSubtaskByCode(int taskCode);

    void clearAllSubtasks();

    void removeSubtaskByCode(int taskCode);

    void updateSubtask(Subtask subtask);

    //эпики
    ArrayList<Epic> getAllEpics();

    void addNewEpic(Epic epic);

    Epic getEpicByCode(int taskCode);

    void removeEpicByCode(int taskCode);

    void updateEpic(Epic epic);

    void clearAllEpics();

    ArrayList<Subtask> getAllSubtasksByEpicCode(int taskCode);

    ArrayList<Task> getHistory();
}