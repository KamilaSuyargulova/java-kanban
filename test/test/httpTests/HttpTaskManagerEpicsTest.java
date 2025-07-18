package test.httpTests;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerEpicsTest extends HttpTaskManagerTestBase {

    @Test
    void testAddEpic() throws IOException, InterruptedException {
        Epic epic = createTestEpic();
        String epicJson = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        var epics = manager.getAllEpics();
        assertEquals(1, epics.size());
        assertEquals("имя", epics.get(0).getTaskName());
    }

    @Test
    void testGetEpicWithSubtasks() throws IOException, InterruptedException {
        Epic epic = createTestEpic();
        manager.addNewEpic(epic);
        Subtask subtask = createTestSubtask(epic);
        manager.addNewSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":" + subtask.getId()));
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = createTestEpic();
        manager.addNewEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertNull(manager.getEpicById(epic.getId()));
    }

}
