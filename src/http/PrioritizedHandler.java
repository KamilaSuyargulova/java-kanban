package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                handleGetPrioritized(exchange);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Task task : prioritized) {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("id", task.getId());
            taskMap.put("taskName", task.getTaskName());
            taskMap.put("type", task.getType());

            if (task.getStartTime() != null) {
                taskMap.put("startTime", task.getStartTime().toString());
                taskMap.put("endTime", task.getEndTime().toString());
            }
            responseList.add(taskMap);
        }
        sendSuccess(exchange, gson.toJson(responseList));
    }
}