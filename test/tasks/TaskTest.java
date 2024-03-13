package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    Task task1 = new Task("1", "1");
    Task task2 = new Task("1", "1");

    //проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void shouldBeEquals() {
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }



}