import entity.User;
import enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    public void testPlayerConstructorAndGetters() {
        String expectedId = "1";
        Color expectedColor = Color.BLACK;

        User player = new User(Integer.parseInt(expectedId), "", "", 1, 1, "");

        assertEquals(expectedId, player.getId());
    }

    @Test
    public void testPlayerWithDifferentId() {
        String expectedId1 = "1";
        String expectedId2 = "2";
        Color expectedColor = Color.WHITE;

        User player1 = new User(Integer.parseInt(expectedId1), "", "", 1, 1, "");
        User player2 = new User(Integer.parseInt(expectedId2), "", "", 1, 1, "");

        assertEquals(expectedId1, player1.getId());
        assertEquals(expectedId2, player2.getId());
    }

    @Test
    public void testPlayerWithDifferentColor() {
        String expectedId = "1";
        Color expectedColor1 = Color.WHITE;
        Color expectedColor2 = Color.BLACK;

        User player1 = new User(Integer.parseInt(expectedId), "", "", 1, 1, "");
        User player2 = new User(Integer.parseInt(expectedId), "", "", 1, 1, "");
    }
}
