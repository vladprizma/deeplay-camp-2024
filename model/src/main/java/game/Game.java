package game;

import entity.Board;
import entity.Player;
import enums.GameStatus;
import enums.PlayerType;
import listener.ReversiListener;

import java.util.HashMap;
import java.util.Map;

public class Game implements ReversiListener {

    private GameLogic gameLogic;
    private String currentPlayerId; // ID текущего игрока
    private Board board;
    private Map<String, Player> players; // Хранение игроков по ID
    private GameStatus gameState;
    private Player aiPlayer;

    public Game() {
        gameStarted();
        playGame();
    }

    // Метод для добавления игрока в игру
    public void addPlayer(String id, String color, PlayerType isAI) {
        if (!players.containsKey(id)) {
            Player player = new Player(id, color, isAI);
            players.put(id, player);
            if (isAI == PlayerType.BOT) {
                // логики с AI пока нет
                aiPlayer = player; // Если это AI, сохраняем ссылку на этого игрока
            }
        } else {
            throw new IllegalArgumentException("Player with ID " + id + " already exists.");
        }
    }

    // основная работа игры
    public void playGame() {
        while (getGameState() == GameStatus.IN_PROGRESS) {
            if (makeMove()) {
                // логика конца игры, пока не готово.

//                boolean isGameOver = gameLogic.checkForWin();
//                if (isGameOver) {
//                    // Завершаем игру и выводим результаты
//                    gameLogic.setGameState(GameStatus.FINISHED);
//                    break;
//                }

                displayBoard();
            }
            playerTurn();
        }

        // не реализовано.
        gameLogic.updateScores();
    }

    public boolean makeMove() {
        return gameLogic.makeMove();
    }

    public void displayBoard() {
        gameLogic.displayBoard();
    }

    public GameStatus getGameState() {
        return gameState;
    }

    public GameStatus setGameState() {
        gameLogic.setGameState(GameStatus.IN_PROGRESS);
        gameState = GameStatus.IN_PROGRESS;
        return gameState;
    }

    // Реализация методов ReversiListener
    @Override
    public boolean moveMade(long playerId, int x, int y) {

        return false;
    }

    @Override
    public boolean moveSkipped(long playerId) {

        return false;
    }

    @Override
    public GameStatus gameFinished() {
        gameState = GameStatus.FINISHED;
        return GameStatus.FINISHED;
    }

    @Override
    public GameStatus gameStarted() {
        players = new HashMap<>(); // создаем мапу для хранения всех игроков. Ключ - Id игрока, значение - сам игрок.
        playerJoin(1); // должен быть примерно такой вызов. Но сама логика метода изменена.
        board = new Board(); // инициализируем доску
        this.gameLogic = new GameLogic(board, players); // запуск модуля игровой логики
        gameLogic.setCurrentPlayer("1");
        setGameState();
        displayBoard();
        return GameStatus.IN_PROGRESS;
    }

    @Override
    public GameStatus gameStopped() {
        gameState = GameStatus.PAUSED;
        return GameStatus.PAUSED;
    }

    @Override
    public GameStatus gameResumed() {
        gameState = GameStatus.IN_PROGRESS;
        return GameStatus.IN_PROGRESS;
    }

    @Override
    public boolean scoreUpdated() {

        return false;
    }

    @Override
    public byte playerTurn() {
        gameLogic.playerTurn();
        return 0;
    }

    @Override
    public boolean playerJoin(long playerId) {
        addPlayer("1", "black", PlayerType.HUMAN);
        addPlayer("2", "white", PlayerType.HUMAN);
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
