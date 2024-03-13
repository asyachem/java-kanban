package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    Subtask subtask1 = new Subtask("1", "1", 1);
    Subtask subtask2 = new Subtask("1", "1", 1);

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void shouldBeEquals() {
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask1, subtask2);
    }
}