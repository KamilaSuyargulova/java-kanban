package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import manager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();

        task = new Task("имя", "описание", 1,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 30, 10, 0));
        taskManager.addNewTask(task);
        epic = new Epic("имя", "описание", 2);
        taskManager.addNewEpic(epic);
        subtask = new Subtask("имя", "описание", 3, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 28, 10, 0));
        taskManager.addNewSubtask(subtask);
    }

    //таски
    @Test
    void shouldAddTask() {
        Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(task, "Задача должна быть добавлена");
        assertEquals(task, savedTask, "Добавленна нужная задача");
    }

    @Test
    void shouldUpdateTask() {
        Task task2 = new Task("имя2", "описание2", task.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 8, 1, 10, 0));
        task2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task2);

        Task task3 = taskManager.getTaskById(task.getId());
        assertEquals("имя2", task3.getTaskName(), "Название задачи должно обновиться");
        assertEquals(TaskStatus.DONE, task3.getTaskStatus(), "Статус задачи должен обновиться");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.removeTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()), "Задача должна удаляться");
    }

    @Test
    void shouldDeleteAllTasks() {
        Task task2 = new Task("имя2", "описание2", 4,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 12, 29, 10, 0));
        taskManager.addNewTask(task2);
        taskManager.clearAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Все задачи должны удаляться");
    }

    //сабтаски
    @Test
    void shouldAddSubtask() {
        Subtask savedSubtask = taskManager.getSubtaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача должна быть добавлена");
        assertEquals(subtask, savedSubtask, "Добавленна нужная подзадача");
    }

    @Test
    void shouldUpdateSubtask() {
        Subtask subtask2 = new Subtask("имя2", "описание2", subtask.getId(),
                subtask.getEpicId(), Duration.ofMinutes(30),
                LocalDateTime.of(2025, 6, 29, 10, 0));
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        Subtask subtask3 = taskManager.getSubtaskById(subtask.getId());
        assertEquals("имя2", subtask3.getTaskName(), "Название подзадачи должно обновиться");
        assertEquals(TaskStatus.DONE, subtask3.getTaskStatus(), "Статус подзадачи должен обновиться");
    }

    @Test
    void shouldDeleteSubtask() {
        taskManager.removeSubtaskById(subtask.getId());
        assertNull(taskManager.getTaskById(subtask.getId()), "Подзадача должна удаляться");
    }

    @Test
    void shouldDeleteAllSubtasks() {
        Subtask subtask2 = new Subtask("имя2", "описание2", 4, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2027, 11, 1, 18, 0));
        taskManager.addNewSubtask(subtask2);
        taskManager.clearAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все подзадачи должны удаляться");
    }

    @Test
    void shouldFindSubtask() {
        Subtask subtask1 = taskManager.getSubtaskById(subtask.getId());

        assertEquals(subtask, subtask1, "Подзадача должна быть найдена через id");
        assertEquals(epic.getId(), subtask1.getEpicId(), "Сабтаск должен хранить номер эпика");
    }

    @Test
    void shouldNotSubtaskWithNonExistentEpic() {
        Subtask subtask1 = new Subtask("имя", "описание", 4, 9,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 2, 10, 0));
        taskManager.addNewSubtask(subtask1);

        assertNull(taskManager.getSubtaskById(subtask1.getId()),
                "Подзадача с несуществующим эпиком не должна добавляться");
    }

    //эпики
    @Test
    void shouldAddEpic() {
        Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик должен быть добавлен");
        assertEquals(epic, savedEpic, "Добавлен нужный эпик");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic2 = new Epic("имя2", "описание2", epic.getId());
        taskManager.updateEpic(epic2);

        Epic epic3 = taskManager.getEpicById(epic.getId());
        assertEquals("имя2", epic3.getTaskName(), "Название подзадачи должно обновиться");
    }

    @Test
    void shouldDeleteEpic() {
        taskManager.removeEpicById(epic.getId());
        assertNull(taskManager.getTaskById(epic.getId()), "Эпик должен удаляться");
        assertNull(taskManager.getSubtaskById(subtask.getId()),
                "Все сабтаски из эпика должны удалиться");
    }

    @Test
    void shouldDeleteAllEpics() {
        Epic epic2 = new Epic("имя2", "описание2", 4);
        taskManager.addNewEpic(epic2);
        taskManager.clearAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Все эпики должны удаляться");
    }

    @Test
    void shouldGetSubtasksByEpicId() {
        ArrayList<Subtask> subtasks = taskManager.getAllSubtasksByEpicId(epic.getId());
        assertFalse(subtasks.isEmpty(), "Должны возвращаться подзадачи эпика");
        assertEquals(epic.getId(), subtasks.get(0).getEpicId(),
                "Должны возвращаться подзадачи из нужного эпика");
    }

    @Test
    void newEpicsShouldHaveStatusNew() {
        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Новый эпик должен иметь статус NEW");
    }

    @Test
    void ifAllSubtasksDoneEpicStatusDone() {
        subtask.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask);

        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Если все подзадачи DONE, то эпик DONE");
    }

    @Test
    void epicStatusShouldBeInProgressWhenDifferentSubtaskStatuses() {
        Subtask subtask1 = new Subtask("имя", "описание", 4, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 11, 29, 10, 0));
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("имя", "описание", 5, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 27, 10, 0));
        subtask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.addNewSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus(),
                "Эпик IN_PROGRESS во всех случаях, кроме всех сабтасков NEW или Done");
    }

    @Test
    void shouldCalculateEpicTimeFromSubtasks() {
        Subtask subtask2 = new Subtask("имя", "описание", 5, epic.getId(),
                Duration.ofMinutes(40), LocalDateTime.of(2025, 6, 28, 12, 0));

        taskManager.addNewSubtask(subtask2);

        assertEquals(subtask.getStartTime(), epic.getStartTime(),
                "Время начала эпика должно равняться времени самой ранней подзадачи");
        assertEquals(subtask2.getEndTime(), epic.getEndTime(),
                "Время окончания эпика должно равняться времени самой поздней подзадачи");
        assertEquals(Duration.ofMinutes(70), epic.getDuration(),
                "Длительность эпика должна быть суммой длительностей подзадач");
    }

    @Test
    void shouldHandleEpicWithNoSubtasks() {
        Epic emptyEpic = new Epic("имя", "описание", 4);
        taskManager.addNewEpic(emptyEpic);

        assertNull(emptyEpic.getStartTime(), "У эпика без подзадач не должно быть времени начала");
        assertNull(emptyEpic.getEndTime(), "У эпика без подзадач не должно быть времени окончания");
        assertNull(emptyEpic.getDuration(), "Длительность эпика без подзадач должна быть 0");
    }

    //история
    @Test
    void shouldAddToHistoryWhenGettingTask() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "В историю должна добавиться задача, эпик и подзадача");
        assertEquals(task, history.get(0), "История должна содержать просмотренную задачу");
        assertEquals(epic, history.get(1), "История должна содержать просмотренный эпик");
        assertEquals(subtask, history.get(2), "История должна содержать просмотренную подзадачу");
    }

    @Test
    void shouldRemoveTaskFromHistoryWhenDeleted() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.clearAllTasks();
        taskManager.clearAllSubtasks();
        taskManager.clearAllEpics();

        assertTrue(taskManager.getHistory().isEmpty(), "История должна быть пустой после удаления всех задач");
    }

    @Test
    void shouldReturnTasksInPriorityOrder() {
        List<Task> prioritized = taskManager.getPrioritizedTasks();

        assertEquals(task, prioritized.get(1), "Задачи должны быть отсортированы по времени");
        assertEquals(subtask, prioritized.get(0), "Задачи должны быть отсортированы по времени");
    }

    @Test
    void shouldThrowWhenTimeOverlaps() {
        Task overlappingTask = new Task("имя", "описание", 4,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 6, 30, 10, 10));

        assertThrows(ManagerValidateException.class,
                () -> taskManager.addNewTask(overlappingTask),
                "Должно выбрасываться исключение при пересечении временных интервалов");
    }

}