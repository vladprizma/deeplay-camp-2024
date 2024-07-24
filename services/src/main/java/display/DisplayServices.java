package display;

import board.BoardLogic;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayServices {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public void display(String currentPlayerId, BoardLogic boardLogic) {
        displayBoard(currentPlayerId, boardLogic);
        displayScore(boardLogic);
    }

    public void displayBoard(String currentPlayerId, BoardLogic boardLogic) {
        logger.info((boardLogic.getBoardState(Integer.parseInt(currentPlayerId))).toString());
    }

    public void displayScore(BoardLogic boardLogic) {
        int[] score = boardLogic.score();
        String scoreText = " Score: " + score[0] + " : " + score[1] + " ";
        int textLength = scoreText.length();
        printTopBorder(textLength);
        logger.info("│" + scoreText + "│");
        printBottomBorder(textLength);
    }

    private static void printTopBorder(int length) {
        logger.info("┌");
        for (int i = 0; i < length; i++) {
            logger.info("─");
        }
        logger.info("┐");
    }

    private static void printBottomBorder(int length) {
        logger.info("[");
        for (int i = 0; i < length; i++) {
            logger.info("─");
        }
        logger.info("]");
    }

    public void displayEndGame(BoardLogic boardLogic) {
        int[] score = boardLogic.score();
        String scoreText;

        if (score[0] > score [1]) {
            scoreText = " Black wins! ";
        } else if (score[0] < score [1]) {
            scoreText = " White wins! ";
        } else {
            scoreText = " draw ";
        }

        int textLength = scoreText.length();
        printTopBorder(textLength);
        logger.info("│" + scoreText + "│");
        printBottomBorder(textLength);
    }
}
