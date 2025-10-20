package http;

import com.google.gson.JsonSyntaxException;
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

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
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
                    } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                        handleGetEpicSubtasks(exchange, pathParts[2]);
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
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        try {
            List<Epic> epics = taskManager.getAllEpics();
            sendSuccess(exchange, gson.toJson(epics));
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                sendSuccess(exchange, gson.toJson(epic));
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Некорректный ID эпика");
        }
    }

    private void handleGetEpicSubtasks(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpicById(id);
            if (epic == null) {
                sendNotFound(exchange);
                return;
            }
            sendSuccess(exchange, gson.toJson(taskManager.getAllSubtasksByEpicId(id)));
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Некорректный ID эпика");
        }
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange);
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);

            if (epic.getTaskName() == null || epic.getTaskName().isEmpty()) {
                sendBadRequest(exchange, "Поле 'taskName' обязательно для эпика");
                return;
            }

            if (epic.getSubtasks() == null) {
                epic.setSubtasks(new ArrayList<>());
            }

            if (epic.getId() == 0) {
                taskManager.addNewEpic(epic);
                Map<String, Object> responseMap = createTaskMap(epic);
                responseMap.put("subtasks", new ArrayList<>());
                sendCreated(exchange, gson.toJson(responseMap));
            } else {
                taskManager.updateEpic(epic);
                Map<String, Object> responseMap = createTaskMap(epic);
                responseMap.put("subtasks", epic.getSubtasks().stream()
                        .map(Subtask::getId)
                        .collect(Collectors.toList()));
                sendCreated(exchange, gson.toJson(responseMap));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "Неверный формат JSON: " + e.getMessage());
        } catch (IllegalStateException e) {
            sendBadRequest(exchange, e.getMessage());
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
            boolean removed = taskManager.removeEpicById(id);
            if (removed) {
                sendSuccess(exchange, "Эпик № " + id + " и его подзадачи удалены");
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Некорректный ID эпика");
        }
    }

}
