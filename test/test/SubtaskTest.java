package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;

class SubtaskTest {

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("имя1", "описание1", 1, 1);
        Subtask subtask2 = new Subtask("имя2", "описание2", 1, 2);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны");
    }
}