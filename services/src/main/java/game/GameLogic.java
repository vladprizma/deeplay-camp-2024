package game;

import bot.BotServices;
import display.DisplayServices;
import entity.Board;
import entity.Bot;
import entity.Player;
import enums.Color;
import enums.GameStatus;
import player.PlayerService;
import repository.ReversiListener;

import java.util.Map;

public class GameLogic implements ReversiListener {

    private BotServices botServices = new BotServices();
    private PlayerService playerService = new PlayerService();
    private DisplayServices displayServices = new DisplayServices();


    public String setCurrentPlayer(String playerId, Map<String, Player> players) {
        if (players.containsKey(playerId)) {
            return playerId;
        } else {
            throw new IllegalArgumentException("Player with ID " + playerId + " does not exist.");
        }
    }

    public boolean checkForWin(Board board) {
        int[] score = board.score();
        boolean endGame =  (score[0] + score[1] == 64) ? true : false;
        return endGame;
    }

    public void display(Board board, String currentPlayerId) {
        displayServices.display(board, currentPlayerId);
    }

    public void displayEndGame(Board board) {
        displayServices.displayEndGame(board);
    }

    @Override
    public boolean moveMade(Board board, Map<String, Player> players, String currentPlayerId) {
        if (players.get(currentPlayerId) instanceof Bot) {
            return botServices.makeMove(board, currentPlayerId);
        } else {
            return playerService.makeMove(board, currentPlayerId);
        }
    }

    @Override
    public boolean moveSkipped(long playerId) {

        return false;
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
    public boolean scoreUpdated() {
        return false;
    }

    @Override
    public String playerTurn(String currentPlayerId, Map<String, Player> players) {
        if (currentPlayerId == "1") {
            return setCurrentPlayer("2", players);
        } else {
            return setCurrentPlayer("1", players);
        }
    }

    @Override
    public boolean playerJoin(Map<String, Player> players, String id, Color color) {
        playerService.addPlayer(players, id, Color.BLACK);
        return false;
    }

    @Override
    public boolean botJoin(Map<String, Player> players, String id, Color color) {
        botServices.addBot(players, id, Color.WHITE);
        return false;
    }

    @Override
    public boolean playerLeave(long playerId) {

        return false;
    }

    @Override
    public boolean playerDisconnect(long playerId) {

        return false;
    }
}
