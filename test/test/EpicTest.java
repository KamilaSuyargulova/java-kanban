package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import tasks.*;

class EpicTest {

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("имя1", "описание1", 1);
        Epic epic2 = new Epic("имя2", "описание2", 1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

}