package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;


class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("имя1", "описание1", 1);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
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

}