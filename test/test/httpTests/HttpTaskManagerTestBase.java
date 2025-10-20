package test.httpTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.BaseHttpHandler;
import http.HttpTaskServer;
import http.timeAdapter.*;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerTestBase {
    protected TaskManager manager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient client;

    public HttpTaskManagerTestBase() {
        this.manager = Managers.getDefault();
        this.gson = createGson();
        this.client = HttpClient.newHttpClient();
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.clearAllTasks();
        manager.clearAllSubtasks();
        manager.clearAllEpics();

        this.taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    protected Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    protected Task createTestTask() {
        Task task = new Task("имя", "описание", 1,
                Duration.ofMinutes(10), null);
        manager.addNewTask(task);
        return task;
    }

    protected Epic createTestEpic() {
        Epic epic = new Epic("имя", "описание", 2);
        manager.addNewEpic(epic);
        return epic;
    }

    protected Subtask createTestSubtask(Epic epic) {
        Subtask subtask = new Subtask("имя", "описание", 3, epic.getId(),
                Duration.ofMinutes(30), null);
        manager.addNewSubtask(subtask);
        return subtask;
    }

}
