package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseHttpHandler {
    protected final Gson gson = new Gson();

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendSuccess(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 200);
    }

    protected void sendCreated(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 201);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Не найдено";
        sendText(exchange, response, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Задача пересекается по времени с другими задачами";
        sendText(exchange, response, 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        String response = "Внутренняя ошибка сервера";
        sendText(exchange, response, 500);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    public void handle(HttpExchange exchange) throws IOException {
    }

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        String response = "Ошибка запроса: " + message;
        sendText(exchange, response, 400);
    }

    protected Map<String, Object> createTaskMap(Task task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("taskName", task.getTaskName());
        taskMap.put("taskDescription", task.getTaskDescription());
        taskMap.put("taskStatus", task.getTaskStatus());
        if (task.getStartTime() != null) {
            taskMap.put("startTime", task.getStartTime().toString());
        }
        if (task.getDuration() != null) {
            taskMap.put("duration", task.getDuration().toMinutes());
        }
        return taskMap;
    }

}
