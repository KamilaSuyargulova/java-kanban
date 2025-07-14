package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        handleGetAllTasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetTaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateTask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 2) {
                        handleDeleteAllTasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleDeleteTaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Task task : tasks) {
            responseList.add(createTaskMap(task));
        }

        sendSuccess(exchange, gson.toJson(responseList));
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                String response = gson.toJson(task);
                sendSuccess(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Map<String, Object> requestMap = gson.fromJson(requestBody, Map.class);

            Task task = new Task(
                    (String) requestMap.get("taskName"),
                    (String) requestMap.get("taskDescription"),
                    ((Double) requestMap.getOrDefault("id", 0.0)).intValue(),
                    requestMap.containsKey("duration")
                            ? Duration.ofMinutes(((Double) requestMap.get("duration")).longValue())
                            : null,
                    requestMap.containsKey("startTime")
                            ? LocalDateTime.parse((String) requestMap.get("startTime"))
                            : null
            );

            if (task.getId() == 0) {
                taskManager.addNewTask(task);
                sendCreated(exchange, gson.toJson(Map.of(
                        "id", task.getId(),
                        "status", "SUCCESS"
                )));
            } else {
                taskManager.updateTask(task);
                sendSuccess(exchange, gson.toJson(Map.of(
                        "status", "UPDATED"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
        taskManager.clearAllTasks();
        sendSuccess(exchange, "Все задачи удалены");
    }

    private void handleDeleteTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.removeTaskById(id);
            sendSuccess(exchange, "Задача №  " + id + " удалена");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
