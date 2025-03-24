package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;

class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("имя1", "описание1", 1);
        Task task2 = new Task("имя2", "описание2", 1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void taskShouldNotBeEqualToNull() {
        Task task = new Task("имя", "описание", 1);
        assertNotEquals(null, task, "Задача не должна быть равна null");
    }
}
