package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.*;
import tasks.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            if (!"GET".equals(method)) {
                sendNotFound(exchange);
                return;
            }
            if ("/history".equals(path)) {
                handleGetHistory(exchange);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();

        List<Map<String, Object>> responseList = history.stream()
                .map(task -> {
                    Map<String, Object> taskMap = createTaskMap(task);
                    if (task instanceof Subtask) {
                        taskMap.put("epicId", ((Subtask) task).getEpicId());
                    }
                    if (task instanceof Epic) {
                        taskMap.put("subtasks", ((Epic) task).getSubtasks().stream()
                                .map(Subtask::getId)
                                .collect(Collectors.toList()));
                    }
                    return taskMap;
                })
                .collect(Collectors.toList());
        sendSuccess(exchange, gson.toJson(responseList));
    }

}
