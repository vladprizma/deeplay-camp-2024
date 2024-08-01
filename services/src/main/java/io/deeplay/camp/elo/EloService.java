package io.deeplay.camp.elo;

import entity.User;

public class EloService {

    private static final int BASE_ELO_CHANGE = 25;
    private static final double K_FACTOR = 32.0;

    public int calculateEloChange(User player, User opponent, boolean playerWon) {
        int playerRating = player.getRating();
        int opponentRating = opponent.getRating();

        double expectedScore = calculateExpectedScore(playerRating, opponentRating);
        double actualScore = playerWon ? 1.0 : 0.0;

        int eloChange = (int) (K_FACTOR * (actualScore - expectedScore));
        return BASE_ELO_CHANGE + eloChange;
    }

    private double calculateExpectedScore(int playerRating, int opponentRating) {
        return 1 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
    }
}
