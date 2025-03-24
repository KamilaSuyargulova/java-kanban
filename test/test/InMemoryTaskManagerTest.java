package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tasks.*;
import manager.*;


class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldNotAddSubtaskToItself() {      //в эпик добавляется сабтаск с другим номером, должен не добавляться ?
        Epic epic = new Epic("имя", "описание", 1);
        taskManager.addNewEpic(epic);

        Subtask subtask = new Subtask("имя", "описание", epic.getTaskCode(), epic.getTaskCode());
        taskManager.addNewSubtask(subtask);

        assertNull(taskManager.getSubtaskByCode(1),
                "Epic нельзя добавлять в самого себя в виде подзадачи");

        assertNull(taskManager.getAllSubtasksByEpicCode(1),
                "Epic нельзя добавлять в самого себя в виде подзадачи");
    }

    @Test
    void shouldNotSubtaskToBeHisOwnEpic() {
        Subtask subtask = new Subtask("имя", "описание", 1, 1);
        taskManager.addNewSubtask(subtask);

        assertTrue(taskManager.getAllSubtasks().isEmpty(),
                "Объект Subtask нельзя сделать своим же эпиком");
    }

}