package io.deeplay.camp;

import entity.Board;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.bot.BotService;
import io.deeplay.camp.game.GameLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BotGameHandler {
    private static final Logger logger = LoggerFactory.getLogger(BotGameHandler.class);
    private static final int GAME_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 10;
    private static final int SCHEDULER_THREAD_COUNT = 10;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_THREAD_COUNT);
    private static final ExecutorService gameExecutor = Executors.newFixedThreadPool(GAME_THREAD_COUNT);
    private final int gameCount;
    private final AtomicInteger firstBotWins = new AtomicInteger(0);
    private final AtomicInteger secondBotWins = new AtomicInteger(0);
    private final AtomicInteger draws = new AtomicInteger(0);
    private final AtomicInteger totalGamesCompleted = new AtomicInteger(0);

    public BotGameHandler(int gameCount) {
        this.gameCount = gameCount;
    }

    public void startBotGame() {
        int totalBatches = (int) Math.ceil((double) gameCount / 50);

        for (int batch = 0; batch < totalBatches; batch++) {
            int gamesInBatch = Math.min(50, gameCount - batch * 50);
            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < gamesInBatch; i++) {
                int gameIndex = batch * 50 + i;
                futures.add(gameExecutor.submit(() -> playSingleGame(gameIndex % 2 == 0)));
            }

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Error during game execution", e);
                }
            }

            logger.info("Batch {} completed. Total games: {}. Bot 1 wins: {}. Bot 2 wins: {}. Draws: {}", batch + 1, totalGamesCompleted.get(), firstBotWins.get(), secondBotWins.get(), draws.get());
        }

        logger.info("Total games: {}. Bot 1 wins: {}. Bot 2 wins: {}. Draws: {}", gameCount, firstBotWins.get(), secondBotWins.get(), draws.get());
        gameExecutor.shutdown();
        scheduler.shutdown();
    }

    private Void playSingleGame(boolean firstBotStarts) {
        BotService firstRandomBot = new BotService();
        BotService secondRandomBot = new BotService();
        Board board = new Board();
        BoardLogic boardLogic = new BoardLogic(board);
        GameLogic gameLogic = new GameLogic(boardLogic);

        boolean gameFinished = false;

        if (firstBotStarts) {
            while (!gameFinished) {
                gameFinished = executeBotMove(firstRandomBot, 1, boardLogic, gameLogic);
                if (!gameFinished) {
                    gameFinished = executeBotMove(secondRandomBot, 2, boardLogic, gameLogic);
                }
            }
        } else {
            while (!gameFinished) {
                gameFinished = executeBotMove(secondRandomBot, 2, boardLogic, gameLogic);
                if (!gameFinished) {
                    gameFinished = executeBotMove(firstRandomBot, 1, boardLogic, gameLogic);
                }
            }
        }

        totalGamesCompleted.incrementAndGet();
        return null;
    }

    private boolean executeBotMove(BotService botService, int botNumber, BoardLogic boardLogic, GameLogic gameLogic) {
        Callable<Boolean> botMoveTask = () -> botService.makeMove(botNumber, boardLogic);
        Future<Boolean> futureMove = scheduler.schedule(botMoveTask, 0, TimeUnit.SECONDS);

        try {
            if (futureMove.get(10, TimeUnit.SECONDS)) {
                return checkWin(boardLogic, gameLogic);
            }
        } catch (TimeoutException e) {
            logger.error("Bot {} move timed out.", botNumber);
            gameFinished(botNumber);
        } catch (Exception e) {
            logger.error("Error during bot {} move", botNumber, e);
            gameFinished(botNumber);
        }

        return true;
    }

    private boolean checkWin(BoardLogic boardLogic, GameLogic gameLogic) {
        if (gameLogic.checkForWin()) {
            int[] scores = boardLogic.score();
            if (scores[0] > scores[1]) {
                firstBotWins.incrementAndGet();
            } else if (scores[0] < scores[1]) {
                secondBotWins.incrementAndGet();
            } else {
                draws.incrementAndGet();
            }
            return true;
        }
        return false;
    }

    private void gameFinished(int botNumberLose) {
        if (botNumberLose == 1) {
            secondBotWins.incrementAndGet();
        } else {
            firstBotWins.incrementAndGet();
        }
        totalGamesCompleted.incrementAndGet();
    }
}
