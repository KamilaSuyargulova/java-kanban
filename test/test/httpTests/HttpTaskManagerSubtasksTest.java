package test.httpTests;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerSubtasksTest extends HttpTaskManagerTestBase {

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = createTestEpic();
        manager.addNewEpic(epic);

        Subtask subtask = createTestSubtask(epic);
        String subtaskJson = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(epic.getId(), subtasks.get(0).getEpicId());
    }

    @Test
    void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = createTestEpic();
        manager.addNewEpic(epic);
        Subtask subtask = createTestSubtask(epic);
        manager.addNewSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"epicId\":" + epic.getId()));
    }

}
