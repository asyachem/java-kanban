package manager;

import history.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
         taskManager = Managers.getDefault();
         historyManager = Managers.getDefaultHistory();
    }

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    public void shouldReturnTaskManager() {
        assertNotNull(taskManager);
    }

    @Test
    public void shouldReturnHistoryManager() {
        assertNotNull(historyManager);
    }

}