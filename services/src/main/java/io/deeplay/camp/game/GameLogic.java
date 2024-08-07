package io.deeplay.camp.game;

import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.bot.BotService;
import io.deeplay.camp.display.DisplayServices;
import entity.Bot;
import entity.User;
import enums.GameStatus;
import io.deeplay.camp.Main;
import io.deeplay.camp.repository.ReversiListener;
import io.deeplay.camp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class GameLogic implements ReversiListener {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private BotService botServices = new BotService();
    private UserService playerService = new UserService();
    private DisplayServices displayServices = new DisplayServices();
    private boolean moveReturn = true;

    private BoardLogic boardLogic;

    public GameLogic(BoardLogic boardLogic) {
        this.boardLogic = boardLogic;
    }

    public boolean checkForWin() {
        int[] score = boardLogic.score();
        boolean endGame =  (score[0] + score[1] == 64) ? true : false;
        return endGame;
    }

    public void display(int currentPlayerId, BoardLogic boardLogic) {
        displayServices.display(currentPlayerId, boardLogic);
    }

    public void displayEndGame(BoardLogic boardLogic) {
        displayServices.displayEndGame(boardLogic);
    }

    @Override 
    public boolean moveMade(User user, int currentPlayerId, BoardLogic boardLogic, String move) {
        if (user instanceof Bot) {
            return botServices.makeMove(currentPlayerId, boardLogic);
        } else {
            return makeUserMove(currentPlayerId, boardLogic, move);
        }
    }

    private boolean makeUserMove(int currentPlayerId, BoardLogic boardLogic, String move) {
        return makeMove(move, currentPlayerId, boardLogic);
    }

    public boolean makeMove(String userInput, int currentPlayerId, BoardLogic boardLogic) {
        int[] move = getCurrentPlayerMove(userInput, currentPlayerId, boardLogic);
        int x = move[0];
        int y = move[1];
        boardLogic.setPiece(x, y, currentPlayerId);
        return moveReturn;
    }

    private int[] getCurrentPlayerMove(String userInput, int currentPlayerId, BoardLogic boardLogic) {
        int[] move;
        moveReturn = true;
        move = getUserMove(userInput);
        logger.info(Integer.toString(move[0]) + " " + Integer.toString(move[1]));
        
        if (boardLogic.isValidMove(move[0], move[1], currentPlayerId)) {
            return new int[]{move[0], move[1]};
        } else {
            logger.info("Данный ход невозможен, попробуйте еще раз.");
            moveReturn = false;
//            getCurrentPlayerMove(userInput, currentPlayerId, boardLogic);
        }
        return new int[]{move[0], move[1]};
    }

    private int[] getUserMove(String userInput) {
        int[] move = new int[2];

        if (userInput.length() == 2 &&
                userInput.charAt(0) >= 'a' && userInput.charAt(0) <= 'h' &&
                userInput.charAt(1) >= '1' && userInput.charAt(1) <= '8') {
            
            logger.error(String.valueOf(userInput.charAt(0)));
            logger.error(String.valueOf(userInput.charAt(1)));
            
            move[0] = userInput.charAt(0) - 'a';
            move[1] = Math.abs(userInput.charAt(1)- '8');
        }
        return move;
    }

    @Override
    public GameStatus gameFinished() {
        return GameStatus.FINISHED;
    }

    @Override
    public GameStatus gameStarted() {
        return GameStatus.IN_PROGRESS;
    }

    @Override
    public GameStatus gameStopped() {
        return GameStatus.PAUSED;
    }

    @Override
    public GameStatus gameResumed() {
        return GameStatus.IN_PROGRESS;
    }

    @Override
    public int[] scoreUpdated() {
        return boardLogic.score();
    }
}
