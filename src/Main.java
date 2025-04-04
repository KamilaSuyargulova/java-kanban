import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Купить яблоки", "", 0);
        taskManager.addNewTask(task1);
        Task task2 = new Task("Записаться на маникюр", "Маникюр в BeBeauty 28/03", 0);
        taskManager.addNewTask(task2);

        Epic epic1 = new Epic("Генеральная уборка", "Уборка в сб или вс", 0);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Помыть полы", "", 0, epic1.getTaskCode());
        Subtask subtask2 = new Subtask("Протереть пыль", "", 0, epic1.getTaskCode());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Закрыть 4 спринт", "", 0);
        taskManager.addNewEpic(epic2);
        Subtask subtask3 = new Subtask("Сдать 4ФЗ", "", 0, epic2.getTaskCode());
        taskManager.addNewSubtask(subtask3);


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

        taskManager.getSubtaskByCode(7);
        subtask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);      //
        taskManager.removeEpicByCode(epic2.getTaskCode());
        taskManager.getTaskByCode(1);
        taskManager.getEpicByCode(3);
        taskManager.getEpicByCode(1); //несуществующий эпик
        taskManager.getSubtaskByCode(7);  //после удаления эпика

        System.out.println("----------------2-----------------");
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

    }

}