package manager;

import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        File file = File.createTempFile("tasks", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        manager.addNewTask(new Task("Записаться на маникюр", "Маникюр в BeBeauty 29.05", 1));
        manager.addNewEpic(new Epic("Генеральная уборка", "Уборка в сб или вс", 2));
        manager.addNewSubtask(new Subtask("Помыть полы", "полы", 3, 2));

        System.out.println("Файл будет сохранён в: " + file.getAbsolutePath());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем, что задачи загрузились
        System.out.println("Tasks: " + loadedManager.getAllTasks());
        System.out.println("Epics: " + loadedManager.getAllEpics());
        System.out.println("Subtasks: " + loadedManager.getAllSubtasks());
    }

    private void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                sb.append(StringConverter.taskToString(task)).append("\n");
            }

            for (Epic epic : getAllEpics()) {
                sb.append(StringConverter.taskToString(epic)).append("\n");
            }

            for (Subtask subtask : getAllSubtasks()) {
                sb.append(StringConverter.taskToString(subtask)).append("\n");
            }

            Files.writeString(file.toPath(), sb.toString()); //создаёт строку, File в Path, записывает
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = StringConverter.taskFromString(lines[i]);
                if (task != null) {
                    if (task.getType().equals(TaskType.EPIC)) {
                        manager.epics.put(task.getId(), (Epic) task);
                    } else if (task.getType().equals(TaskType.SUBTASK)) {
                        manager.subtasks.put(task.getId(), (Subtask) task);
                        Epic epic = manager.epics.get(((Subtask) task).getEpicId());
                        if (epic != null) {
                            epic.addSubtask((Subtask) task);
                        }
                    } else {
                        manager.tasks.put(task.getId(), task);
                    }
                    manager.id = Math.max(manager.id, task.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
        return manager;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}
