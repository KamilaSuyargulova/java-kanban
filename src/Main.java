import java.util.Scanner;

public class Main {
    static TaskManager taskManager = new TaskManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        taskManager.addNewTask("Купить яблоки", "");
        taskManager.addNewTask("Записаться на маникюр", "Маникюр в BeBeauty 28/03 после 18-00");
        taskManager.addNewEpic("Генеральная уборка", "Уборка в сб или вс, вместо Марины");
        taskManager.addSubtask("Помыть полы", "", 3);
        taskManager.addSubtask("Протереть пыль", "", 3);
        taskManager.addNewEpic("Закрыть 4 спринт", "");
        taskManager.addSubtask("Сдать 4ФЗ", "", 6);

        while (true) {
            printMenu();
            String command = scanner.nextLine();

            switch (command) {
                case "1" -> taskManager.printAllTasks();
                case "2" -> addNewTask();
                case "3" -> taskSearch(); // реализовать
                case "4" -> taskManager.clearAllTasks();

                case "0" -> {
                    System.out.println("Выход");
                    return;
                }
                default -> System.out.println("Такой комманды нет. Повторите попытку.");
            }
        }
    }


    public static void printMenu() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Добавить задачу");
        System.out.println("3 — Выбрать задачу");
        System.out.println("4 — Удалить все задачи");
        System.out.println("0 — Выход");
    }


    public static void addNewTask() {
        String taskName;
        String taskDescription;
        System.out.println("Вы ходите добавить:");
        System.out.println("1 - Новую задачу");
        System.out.println("2 - Создать новый эпик");
        System.out.println("3 - Добавить подзадачу в эпик");
        String command = scanner.nextLine();
        switch (command) {
            case "1" -> {
                System.out.println("Введите название задачи:");
                taskName = scanner.nextLine();
                System.out.println("Введите описание задачи:");
                taskDescription = scanner.nextLine();
                taskManager.addNewTask(taskName, taskDescription);
                System.out.println("Задача успешно добавлена!");
            }
            case "2" -> {
                System.out.println("Введите название эпика");
                taskName = scanner.nextLine();
                System.out.println("Введите описание эпика:");
                taskDescription = scanner.nextLine();
                taskManager.addNewEpic(taskName, taskDescription);
            }
            case "3" -> {
                System.out.println("Введите номер эпика:");
                int epicCode = scanner.nextInt();
                scanner.nextLine();
                if (taskManager.containsEpic(epicCode)) {
                    System.out.println("Введите название подзадачи:");
                    taskName = scanner.nextLine();
                    System.out.println("Введите описание подзадачи:");
                    taskDescription = scanner.nextLine();
                    taskManager.addSubtask(taskName, taskDescription, epicCode);
                } else {
                    System.out.println("Эпик с таком номером не найден");
                }
            }
            default -> System.out.println("Ошибка");

        }
    }

    public static void taskSearch() {
        Task isTask = null;
        System.out.println("Выберите способ поиска :");
        System.out.println("1 - По номеру ");
        System.out.println("2 - По названию ");
        String command = scanner.next();
        scanner.nextLine();
        switch (command) {
            case "1" -> {
                System.out.println("Введите номер задачи:");
                int taskCode = scanner.nextInt();
                scanner.nextLine();
                isTask = taskManager.searchByTaskCode(taskCode);
            }
            case "2" -> {
                System.out.println("Введите название задачи");
                String taskName = scanner.nextLine();
                isTask = taskManager.searchByTaskName(taskName);
            }
            default -> System.out.println("Ошибка");
        }

        if (isTask == null) {
            System.out.println("Задача не найдена");
            return;
        }
        System.out.println("Задача найдена: ");
        System.out.println(isTask);

        System.out.println("Выберите дальнейшие действия:");
        System.out.println("1 - Изменить содержание задачи");
        System.out.println("2 - Изменить статус задачи");
        System.out.println("3 - Удалить задачу");
        command = scanner.next();
        scanner.nextLine();
        switch (command) {
            case "1" -> System.out.println("1 - Изменить содержание задачи");
            case "2" -> {
                System.out.println("Выберите новый статус :");
                System.out.println("1 - Над задачей ведётся работа");
                System.out.println("2 - Задача выполнена");
                command = scanner.next();
                scanner.nextLine();
                switch (command) {
                    case "1" -> taskManager.changeStatus(isTask, TaskStatus.IN_PROGRESS);
                    case "2" -> taskManager.changeStatus(isTask, TaskStatus.DONE);
                    default -> System.out.println("Ошибка");
                }
            }
            case "3" -> taskManager.removeTask(isTask.getTaskCode());
            default -> System.out.println("Ошибка");
        }
    }

}






