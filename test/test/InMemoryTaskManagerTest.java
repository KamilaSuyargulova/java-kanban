package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    //таски
    @Test
    void shouldAddAndFindTask() {
        Task task = new Task("имя", "описание", 1);
        taskManager.addNewTask(task);

        Task task1 = taskManager.getTaskByCode(task.getTaskCode());

        assertNotNull(task, "Задача должна быть добавлена");
        assertNotNull(task1, "Задача должна быть найдена через TaskCode");
        assertEquals(task, task1, "Задачи с одинаковым taskCode должны совпадать");
    }

    @Test
    void shouldUpdateTask() {
        Task task1 = new Task("имя1", "описание1", 1);
        taskManager.addNewTask(task1);

        Task task2 = new Task("имя2", "описание2", task1.getTaskCode());
        task2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task2);

        Task task = taskManager.getTaskByCode(task1.getTaskCode());
        assertEquals("имя2", task.getTaskName(), "Название задачи должно обновиться");
        assertEquals(TaskStatus.DONE, task.getTaskStatus(), "Статус задачи должен обновиться");
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("имя", "описание", 1);
        taskManager.addNewTask(task);

        taskManager.removeTaskByCode(task.getTaskCode());
        assertNull(taskManager.getTaskByCode(task.getTaskCode()), "Задача должна удаляться");
    }

    //сабтаски
    @Test
    void shouldAddAndFindSubtask() {
        Epic epic = new Epic("имя", "описание", 1);
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("имя", "описание", 2, epic.getTaskCode());
        taskManager.addNewSubtask(subtask);

        Subtask subtask1 = taskManager.getSubtaskByCode(subtask.getTaskCode());
        assertEquals(subtask, subtask1, "Подзадача должна быть найдена через TaskCode");
        assertEquals(epic.getTaskCode(), subtask1.getEpicCode(), "Сабтаск должен хранить номер эпика");
    }

    @Test
    void shouldNotSubtaskToBeHisOwnEpic() {
        Subtask subtask = new Subtask("имя", "описание", 1, 1);
        taskManager.addNewSubtask(subtask);

        assertTrue(taskManager.getAllSubtasks().isEmpty(),
                "Подзадача с несуществующим эпиком не должна добавляться, ее нельзя сделать своим же эпиком");
    }

    //эпики
    @Test
    void shouldAddAndFindNewEpicStatusNew() {
        Epic epic = new Epic("имя", "описание", 1);
        taskManager.addNewEpic(epic);

        Epic epic1 = taskManager.getEpicByCode(epic.getTaskCode());
        assertEquals(epic, epic1, "Задача должна быть найдена через TaskCode");
        assertEquals(TaskStatus.NEW, epic1.getTaskStatus(), "Новый эпик должен иметь статус NEW");
    }

    @Test
    void ifAllSubtasksDoneEpicStatusDone() {
        Epic epic = new Epic("имя", "описание", 1);
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("имя", "описание", 2, epic.getTaskCode());
        subtask.setTaskStatus(TaskStatus.DONE);
        taskManager.addNewSubtask(subtask);

        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Если все подзадачи DONE, то эпик DONE");
    }

    @Test
    void epicStatusShouldBeInProgressWhenDifferentSubtaskStatuses() {
        Epic epic = new Epic("имя", "описание", 1);
        taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("имя", "описание", 2, epic.getTaskCode());
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("имя", "описание", 2, epic.getTaskCode());
        subtask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(),
                "Эпик IN_PROGRESS во всех случаях, кроме всех сабтасков NEW или Done");
    }

    //история
    @Test
    void shouldAddToHistoryWhenGettingTask() {
        Task task = new Task("имя", "описание", 1);
        taskManager.addNewTask(task);
        Epic epic = new Epic("имя", "описание", 2);
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("имя", "описание", 3, epic.getTaskCode());
        taskManager.addNewSubtask(subtask);

        taskManager.getTaskByCode(task.getTaskCode());
        taskManager.getEpicByCode(epic.getTaskCode());
        taskManager.getSubtaskByCode(subtask.getTaskCode());
        ArrayList<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "В историю должна добавиться задача, эпик и подзадача");
        assertEquals(task, history.get(0), "История должна содержать просмотренную задачу");
        assertEquals(epic, history.get(1), "История должна содержать просмотренный эпик");
        assertEquals(subtask, history.get(2), "История должна содержать просмотренную подзадачу");
    }

}