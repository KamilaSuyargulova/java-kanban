package http;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager taskManager) {
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
        sendSuccess(exchange, gson.toJson(taskManager.getAllTasks()));
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                sendSuccess(exchange, gson.toJson(task));
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
            Task task = gson.fromJson(requestBody, Task.class);
            if (task.getTaskName() == null || task.getTaskName().isEmpty()) {
                sendBadRequest(exchange, "Поле 'taskName' обязательно");
                return;
            }
            if (task.getId() == 0) {
                taskManager.addNewTask(task);
                sendCreated(exchange, gson.toJson(createTaskMap(task)));
            } else {
                taskManager.updateTask(task);
                sendCreated(exchange, gson.toJson(createTaskMap(task)));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "Неверный формат JSON: " + e.getMessage());
        } catch (ManagerValidateException e) {
            sendHasInteractions(exchange);
        } catch (IllegalStateException e) {
            sendBadRequest(exchange, e.getMessage());
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
            boolean removed = taskManager.removeTaskById(id);
            if (removed) {
                sendSuccess(exchange, "Задача № " + id + " удалена");
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

}
