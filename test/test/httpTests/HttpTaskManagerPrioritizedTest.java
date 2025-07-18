package test.httpTests;

import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerPrioritizedTest extends HttpTaskManagerTestBase {

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task = createTestTask();
        manager.addNewTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertTrue(response.body().contains("\"id\":" + task.getId()));
    }

    @Test
    void testPrioritizedOrder() throws IOException, InterruptedException {
        Task task1 = new Task("позже", "описание", 1,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 8, 11, 10, 0));
        Task task2 = new Task("раньше", "описание", 1,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 8, 10, 10, 0));

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        int indexTask2 = response.body().indexOf("\"id\":" + task2.getId());
        int indexTask1 = response.body().indexOf("\"id\":" + task1.getId());
        assertTrue(indexTask2 < indexTask1);
    }

}