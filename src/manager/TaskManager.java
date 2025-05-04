package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //задачи
    ArrayList<Task> getAllTasks();

    int addNewTask(Task task);

    Task getTaskById(int id);

    void clearAllTasks();

    void removeTaskById(int id);

    void updateTask(Task task);

    //подзадачи
    ArrayList<Subtask> getAllSubtasks();

    void addNewSubtask(Subtask subtask);

    Subtask getSubtaskById(int id);

    void clearAllSubtasks();

    void removeSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    //эпики
    ArrayList<Epic> getAllEpics();

    void addNewEpic(Epic epic);

    Epic getEpicById(int id);

    void removeEpicById(int id);

    void updateEpic(Epic epic);

    void clearAllEpics();

    ArrayList<Subtask> getAllSubtasksByEpicId(int id);

    List<Task> getHistory();
}