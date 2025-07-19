package http;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;
import java.util.Map;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
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
        sendSuccess(exchange, gson.toJson(taskManager.getAllSubtasks()));
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                sendSuccess(exchange, gson.toJson(subtask));
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
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            if (subtask == null) {
                sendBadRequest(exchange, "Неверный формат данных подзадачи");
                return;
            }

            if (subtask.getTaskName() == null || subtask.getTaskName().isEmpty()) {
                sendBadRequest(exchange, "Название подзадачи обязательно");
                return;
            }
            if (subtask.getEpicId() == 0) {
                sendBadRequest(exchange, "Не указан ID эпика");
                return;
            }

            Epic epic = taskManager.getEpicById(subtask.getEpicId());
            if (epic == null) {
                sendNotFound(exchange);
                return;
            }

            try {
                if (!canAddSubtaskToEpic(epic)) {
                    sendBadRequest(exchange, "Эпик не готов к добавлению подзадач");
                    return;
                }
            } catch (NullPointerException e) {
                sendBadRequest(exchange, "Некорректное состояние эпика");
                return;
            }

            if (subtask.getId() == 0) {
                taskManager.addNewSubtask(subtask);
                sendCreated(exchange, createSuccessResponse(subtask));
            } else {
                taskManager.updateSubtask(subtask);
                sendCreated(exchange, createSuccessResponse(subtask));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "Ошибка в формате JSON");
        } catch (ManagerValidateException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private boolean canAddSubtaskToEpic(Epic epic) {
        try {
            if (epic.getSubtasks() == null) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private String createSuccessResponse(Subtask subtask) {
        Map<String, Object> response = createTaskMap(subtask);
        response.put("epicId", subtask.getEpicId());
        return gson.toJson(response);
    }

    private void handleDeleteAllSubtasks(HttpExchange exchange) throws IOException {
        taskManager.clearAllSubtasks();
        sendSuccess(exchange, "Все сабтаски удалены");
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            boolean removed = taskManager.removeSubtaskById(id);
            if (removed) {
                sendSuccess(exchange, "Подзадача № " + id + " удалена");
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Некорректный ID подзадачи");
        }
    }

}

