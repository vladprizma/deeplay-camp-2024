package display;

import board.BoardLogic;
import entity.Board;

public class DisplayServices {

    public void display(Board board, String currentPlayerId, BoardLogic boardLogic) {
        displayBoard(board, currentPlayerId, boardLogic);
        displayScore(board, boardLogic);
    }

    public void displayBoard(Board board, String currentPlayerId, BoardLogic boardLogic) {
        System.out.printf((boardLogic.getBoardState(Integer.parseInt(currentPlayerId))).toString());
    }

    public void displayScore(Board board, BoardLogic boardLogic) {
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

    public void displayEndGame(Board board, BoardLogic boardLogic) {
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
