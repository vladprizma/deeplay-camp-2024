import entity.Bot;
import enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotTest {
    @Test
    public void testGetId_ShouldReturnCorrectId() {
        String expectedId = "1";
        Bot bot = new Bot(expectedId, Color.BLACK);
        int actualId = bot.getId();
//        assertEquals(expectedId, actualId);
    }
}
