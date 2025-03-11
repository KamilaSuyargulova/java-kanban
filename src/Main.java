import java.util.Scanner;

public class Main {
    static TaskManager taskManager = new TaskManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

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
                System.out.println("Введите название подзадачи:");
                taskName = scanner.nextLine();
                System.out.println("Введите описание подзадачи:");
                taskDescription = scanner.nextLine();
                taskManager.addSubtask(taskName, taskDescription, epicCode);
            }

            default -> System.out.println("Ошибка");

        }
    }

    public static void taskSearch() {
        Task foundTask;
        // реализация поиска и вывод информации
        System.out.println("Выберите дальнейшие действия:");
        System.out.println("1 - Изменить содержание задачи");
        System.out.println("2 - Изменить статус задачи");
        System.out.println("3 - Удалить задачу");
        System.out.println("4 - Вернуться к главному меню");
        String command = scanner.next();
        switch (command) {
            case "1" -> System.out.println("1 - Изменить содержание задачи");
            case "2" -> System.out.println("2 - Изменить статус задачи");
            case "3" -> System.out.println("3 - Удалить задачу");
            case "4" -> System.out.println("4 - Вернуться к главному меню");
            default -> System.out.println("Ошибка");
        }
    }

}




