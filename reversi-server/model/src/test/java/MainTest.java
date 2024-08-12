import io.deeplay.camp.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {
    @Test
    public void testExample() {
        assertDoesNotThrow(Main::new);
    }
}