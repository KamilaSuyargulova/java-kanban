package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;

import java.util.List;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("имя1", "описание1", 1);
        task2 = new Task("имя2", "описание2", 2);
        task3 = new Task("имя3", "описание3", 3);
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldAddTasksToTheEndOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task1, history.get(0), "Первая в списке задача № 1");
        assertEquals(task2, history.get(1), "Вторая в списке задача № 2");
    }

    @Test
    void shouldNotContainDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task2, history.get(0), "Первая в списке задача № 2");
        assertEquals(task1, history.get(1), "Повторное добавление задачи № 1 в конец списка");
    }

    @Test
    void shouldIgnoreNull() {
        historyManager.add(null);

        assertTrue(historyManager.getHistory().isEmpty(), "История должна игнорировать null");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.remove(1);

        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустая после удаления");
    }

    @Test
    void shouldRemoveFromBeginningAndSaveCorrectOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task2, task3), historyManager.getHistory(),
                "Все задачи станут выше в списке после удления №1");
    }

    @Test
    void shouldRemoveFromMiddleAndSaveCorrectOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task1, task3), historyManager.getHistory(),
                "Задачи после удаленной станут выше в списке");
    }


    @Test
    void shouldRemoveFromEndAndSaveCorrectOrder() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(List.of(task1, task2), historyManager.getHistory(),
                "Задачи на месте после удаления последней");
    }

}
