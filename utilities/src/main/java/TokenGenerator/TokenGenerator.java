package TokenGenerator;

import java.util.UUID;

public class TokenGenerator {

    public static String generateID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static void main(String[] args) {
        String player1Token = generateID();
        String player2Token = generateID();

        System.out.println("Player 1 token: " + player1Token);
        System.out.println("Player 2 token: " + player2Token);
    }
}
