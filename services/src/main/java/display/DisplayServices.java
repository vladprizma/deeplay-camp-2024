package display;

import board.BoardLogic;

public class DisplayServices {

    public void display(String currentPlayerId, BoardLogic boardLogic) {
        displayBoard(currentPlayerId, boardLogic);
        displayScore(boardLogic);
    }

    public void displayBoard(String currentPlayerId, BoardLogic boardLogic) {
        System.out.printf((boardLogic.getBoardState(Integer.parseInt(currentPlayerId))).toString());
    }

    public void displayScore(BoardLogic boardLogic) {
        int[] score = boardLogic.score();
        String scoreText = " Score: " + score[0] + " : " + score[1] + " ";
        int textLength = scoreText.length();
        printTopBorder(textLength);
        System.out.println("│" + scoreText + "│");
        printBottomBorder(textLength);
    }

    private static void printTopBorder(int length) {
        System.out.print("┌");
        for (int i = 0; i < length; i++) {
            System.out.print("─");
        }
        System.out.println("┐");
    }

    private static void printBottomBorder(int length) {
        System.out.print("[");
        for (int i = 0; i < length; i++) {
            System.out.print("─");
        }
        System.out.println("]");
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
        System.out.println("│" + scoreText + "│");
        printBottomBorder(textLength);
    }
}
