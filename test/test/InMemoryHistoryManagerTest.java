package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("имя1", "описание1", 1);
        task2 = new Task("имя2", "описание2", 2);
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldAddTasksToTheEndOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        ArrayList<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task1, history.get(0), "Первая в списке задача № 1");
        assertEquals(task2, history.get(1), "Вторая в списке задача № 2");
    }

    @Test
    void shouldAddTasksDuplicateToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        ArrayList<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertEquals(task1, history.get(0), "Первая в списке задача № 1");
        assertEquals(task2, history.get(1), "Вторая в списке задача № 2");
        assertEquals(task1, history.get(2), "Повторное добавление задачи № 1 в конец списка");
    }

    @Test
    void shouldLimitHistorySize() {
        for (int i = 0; i < 12; i++) {
            Task t = new Task("имя1", "описание1", i);
            historyManager.add(t);
        }

        assertEquals(10, historyManager.getHistory().size(),
                "Максимальное кол-во задач в истории - 10");
    }

    @Test
    void shouldIgnoreNull() {
        historyManager.add(null);

        assertTrue(historyManager.getHistory().isEmpty(), "История должна игнорировать null");
    }

}