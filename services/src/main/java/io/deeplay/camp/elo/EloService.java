package io.deeplay.camp.elo;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for handling Elo rating calculations.
 * <p>
 * This class provides methods for calculating the change in Elo rating for a player based on the outcome of a match.
 * It uses a base Elo change value and a K-factor to adjust the rating change.
 * </p>
 */
public class EloService {

    private static final Logger logger = LoggerFactory.getLogger(EloService.class);
    private static final int BASE_ELO_CHANGE = 25;
    private static final double K_FACTOR = 32.0;

    /**
     * Calculates the change in Elo rating for a player.
     * <p>
     * This method calculates the Elo rating change for a player based on their current rating, the opponent's rating,
     * and whether the player won the match. It uses the K-factor and a base Elo change value to determine the final change.
     * </p>
     *
     * @param player    The player whose Elo rating is to be calculated.
     * @param opponent  The opponent player.
     * @param playerWon True if the player won the match, false otherwise.
     * @return The change in Elo rating for the player.
     */
    public int calculateEloChange(User player, User opponent, boolean playerWon) {
        logger.info("Calculating Elo change for player: {} against opponent: {}", player.getUsername(), opponent.getUsername());
        int playerRating = player.getRating();
        int opponentRating = opponent.getRating();

        double expectedScore = calculateExpectedScore(playerRating, opponentRating);
        double actualScore = playerWon ? 1.0 : 0.0;

        int eloChange = (int) (K_FACTOR * (actualScore - expectedScore));
        return BASE_ELO_CHANGE + eloChange;
    }

    /**
     * Calculates the expected score for a player.
     * <p>
     * This method calculates the expected score for a player based on their rating and the opponent's rating.
     * The expected score is used to determine the Elo rating change.
     * </p>
     *
     * @param playerRating   The rating of the player.
     * @param opponentRating The rating of the opponent.
     * @return The expected score for the player.
     */
    private double calculateExpectedScore(int playerRating, int opponentRating) {
        logger.info("Calculating expected score for player rating: {} against opponent rating: {}", playerRating, opponentRating);
        return 1 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
    }
}