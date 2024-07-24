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
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetColor_ShouldReturnCorrectColor() {
        Color expectedColor = Color.WHITE;
        Bot bot = new Bot("bot2", expectedColor);
        Color actualColor = bot.getColor();
        assertEquals(expectedColor, actualColor);
    }
}
