package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import manager.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            file = Files.createTempFile("tasks", ".csv").toFile();
            return new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл", e);
        }
    }

    @Test
    void shouldSaveAndLoadTasks() throws IOException {
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

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(emptyFile);

        assertTrue(loadedManager.getAllTasks().isEmpty(), "Файл должен быть пустым");
        assertTrue(loadedManager.getAllEpics().isEmpty(), "Файл должен быть пустым");
        assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Файл должен быть пустым");
    }

}