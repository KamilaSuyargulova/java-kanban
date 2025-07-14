package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
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
                        handleGetAllEpics(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetEpicById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    handleCreateOrUpdateEpic(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 2) {
                        handleDeleteAllEpics(exchange);
                    } else if (pathParts.length == 3) {
                        handleDeleteEpicById(exchange, pathParts[2]);
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

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getAllEpics();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Epic epic : epics) {
            Map<String, Object> epicMap = createTaskMap(epic);
            epicMap.put("subtasks", epic.getSubtasks().stream()
                    .map(Subtask::getId)
                    .collect(Collectors.toList()));
            responseList.add(epicMap);
        }

        sendSuccess(exchange, gson.toJson(responseList));
    }

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                String response = gson.toJson(epic);
                sendSuccess(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            List<Subtask> subtasks = taskManager.getAllSubtasksByEpicId(id);
            String response = gson.toJson(subtasks);
            sendSuccess(exchange, response);
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Map<String, Object> requestMap = gson.fromJson(requestBody, Map.class);

            Epic epic = new Epic(
                    (String) requestMap.get("taskName"),
                    (String) requestMap.get("taskDescription"),
                    ((Double) requestMap.getOrDefault("id", 0.0)).intValue()
            );

            if (epic.getId() == 0) {
                taskManager.addNewEpic(epic);
                sendCreated(exchange, gson.toJson(Map.of(
                        "id", epic.getId(),
                        "status", "SUCCESS"
                )));
            } else {
                taskManager.updateEpic(epic);
                sendSuccess(exchange, gson.toJson(Map.of(
                        "status", "UPDATED"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleDeleteAllEpics(HttpExchange exchange) throws IOException {
        taskManager.clearAllEpics();
        sendSuccess(exchange, "Все эпики удалены");
    }

    private void handleDeleteEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.removeEpicById(id);
            sendSuccess(exchange, "Эпик № " + id + " удален");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
