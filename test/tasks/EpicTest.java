package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1 = new Epic("1", "1");
    Epic epic2 = new Epic("1", "1");

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void shouldBeEquals() {
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2);
    }

    // Тест: проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    // тест выше не писала, тк в методе addSubtask в параметрах два объекта с типами Subtask и Epic. и так выдает ошибку

    //Тест: проверьте, что объект Subtask нельзя сделать своим же эпиком;
    // аналогично и с этим тестом

}