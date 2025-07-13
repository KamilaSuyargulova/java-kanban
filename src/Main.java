import manager.Managers;
import manager.TaskManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Купить яблоки", "", 0,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 29, 10, 0));
        taskManager.addNewTask(task1);
        Task task2 = new Task("Записаться на маникюр", "Маникюр в BeBeauty 29.05", 0,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 7, 5, 16, 55));
        taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Генеральная уборка", "Уборка в сб или вс", 0);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Помыть полы", "", 0, epic1.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 7, 5, 16, 0));
        Subtask subtask2 = new Subtask("Протереть пыль", "", 0, epic1.getId(),
                Duration.ofMinutes(15), LocalDateTime.of(2025, 7, 5, 8, 0));
        Subtask subtask3 = new Subtask("Помыть окна", "", 0, epic1.getId(),
                Duration.ofMinutes(45), LocalDateTime.of(2025, 7, 5, 14, 0));
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        Epic epic2 = new Epic("Закрыть 6 спринт", "", 0);
        taskManager.addNewEpic(epic2);


        System.out.println("----------------1-----------------");
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Все эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("Все подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История просмотров: ");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("----------------2-----------------");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);

        System.out.println("История просмотров: ");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Список задач по приоритетам: ");
        taskManager.getPrioritizedTasks().forEach(System.out::println);

    }

}