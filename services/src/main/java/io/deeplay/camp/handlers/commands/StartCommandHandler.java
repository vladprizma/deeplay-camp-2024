package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.game.GameLogic;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * CommandHandler для начала игры.
 * Позволяет найти или создать игровую сессию, настроить логику игры и уведомить игрока о начале игры.
 */
public class StartCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(StartCommandHandler.class);
    private static final int MAX_WAIT_TIME_MS = 300000; // Максимальное время ожидания в миллисекундах

    /**
     * Обрабатывает команду начала игры.
     *
     * @param message     Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException          В случае ошибки ввода-вывода.
     * @throws InterruptedException В случае прерывания потока.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, InterruptedException {
        if (!mainHandler.isLogin()) {
            mainHandler.sendMessageToClient("Please login or register.");
            return;
        }

        logger.info("User {} is attempting to start a game.", mainHandler.getUser().getId());

        var result = SessionManager.getInstance().findOrCreateSession(mainHandler, mainHandler.getUser());
        mainHandler.setSession(result.getGameSession());

        int waitTime = 0;
        while (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED && waitTime < MAX_WAIT_TIME_MS) {
            Thread.sleep(1000);
            waitTime += 1000;
        }

        if (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED) {
            mainHandler.sendMessageToClient("Failed to start the game. Please try again later.");
            logger.info("User {}: Game start failed due to timeout.", mainHandler.getUser().getId());
            return;
        }

        mainHandler.setBoardLogic(new BoardLogic(result.getGameSession().getBoard()));
        mainHandler.setGameLogic(new GameLogic(mainHandler.getBoardLogic()));

        logger.info("User {}: The enemy was found. The game begins...", mainHandler.getUser().getId());

        var opponent = SessionManager.getInstance().getOpponent(mainHandler);
        logger.info("efreferfer" + String.format("session::%d %s %d %d %s %d",
                opponent.getId(), opponent.getUserPhoto(), opponent.getRating(),
                opponent.getMatches(), opponent.getUsername(), opponent.getElo()));
        mainHandler.sendMessageToClient(String.format("session::%d %s %d %d %s %d",
                opponent.getId(), opponent.getUserPhoto(), opponent.getRating(),
                opponent.getMatches(), opponent.getUsername(), opponent.getElo()));

        var userId = mainHandler.getUser().getId();
        int playerNumber = (userId == mainHandler.getSession().getPlayer1().getId()) ? 1 : 2;

        mainHandler.getGameLogic().display(playerNumber, mainHandler.getBoardLogic());
    }
}