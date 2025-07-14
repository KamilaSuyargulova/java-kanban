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

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                        handleGetAllSubtasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetSubtaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateSubtask(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 2) {
                        handleDeleteAllSubtasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleDeleteSubtaskById(exchange, pathParts[2]);
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

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Subtask subtask : subtasks) {
            Map<String, Object> subtaskMap = createTaskMap(subtask);
            subtaskMap.put("epicId", subtask.getEpicId());
            responseList.add(subtaskMap);
        }

        sendSuccess(exchange, gson.toJson(responseList));
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                String response = gson.toJson(subtask);
                sendSuccess(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Map<String, Object> requestMap = gson.fromJson(requestBody, Map.class);

            if (!requestMap.containsKey("epicId")) {
                sendBadRequest(exchange, "Поле 'epicId' обязательно для подзадачи");
                return;
            }

            Subtask subtask = new Subtask(
                    (String) requestMap.get("taskName"),
                    (String) requestMap.get("taskDescription"),
                    ((Double) requestMap.getOrDefault("id", 0.0)).intValue(),
                    ((Double) requestMap.get("epicId")).intValue(),
                    requestMap.containsKey("duration")
                            ? Duration.ofMinutes(((Double) requestMap.get("duration")).longValue())
                            : null,
                    requestMap.containsKey("startTime")
                            ? LocalDateTime.parse((String) requestMap.get("startTime"))
                            : null
            );

            if (subtask.getId() == 0) {
                taskManager.addNewSubtask(subtask);
                sendCreated(exchange, gson.toJson(Map.of(
                        "id", subtask.getId(),
                        "status", "SUCCESS"
                )));
            } else {
                taskManager.updateSubtask(subtask);
                sendSuccess(exchange, gson.toJson(Map.of(
                        "status", "UPDATED"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
        taskManager.clearAllSubtasks();
        sendSuccess(exchange, "Все сабтаски удалены");
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.removeSubtaskById(id);
            sendSuccess(exchange, "Сабтаск №  " + id + " удален");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

}
