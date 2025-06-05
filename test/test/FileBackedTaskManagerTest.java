package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import manager.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;
    private File file;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() throws IOException {
        file = Files.createTempFile("tasks", ".csv").toFile();
        taskManager = new FileBackedTaskManager(file);

        task = new Task("имя", "описание", 1);
        taskManager.addNewTask(task);
        epic = new Epic("имя", "описание", 2);
        taskManager.addNewEpic(epic);
        subtask = new Subtask("имя", "описание", 3, epic.getId());
        taskManager.addNewSubtask(subtask);
    }

    @Test
    void shouldSaveAndLoadTasks() throws IOException {
        taskManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(taskManager.getAllTasks(), loadedManager.getAllTasks(), "Все задачи должны сохраниться");
        assertEquals(taskManager.getAllEpics(), loadedManager.getAllEpics(), "Все зэпики должны сохраниться");
        assertEquals(taskManager.getAllSubtasks(), loadedManager.getAllSubtasks(),
                "Все подзадачи должны сохраниться");
    }

    @Test
    void shouldSaveAndLoadEmptyManager() throws IOException {
        File emptyFile = Files.createTempFile("empty", ".csv").toFile();
        FileBackedTaskManager emptyManager = new FileBackedTaskManager(emptyFile);
        emptyManager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(emptyFile);

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Файл должен быть пустым");
        assertTrue(loadedManager.getAllEpics().isEmpty(), "Файл должен быть пустым");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Файл должен быть пустым");
    }

}
