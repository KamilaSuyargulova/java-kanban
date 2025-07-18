package http;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {                //При проверке работы в insomnia всплыло много недочетов
    private static final int PORT = 8080;   //пришлось нестколько раз переписать класы пакета http и немного старых
    private final HttpServer server;       //боюсь что-то еще менять
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.taskManager = manager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Завершение работы сервера");
    }

}