package io.deeplay.camp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entity.Board;
import io.deeplay.camp.board.BoardLogic;
import io.deeplay.camp.bot.RandomBot;
import io.deeplay.camp.game.GameLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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

        saveResultsToJson();
    }

    private Void playSingleGame(boolean firstBotStarts) {
        RandomBot firstRandomBot = new RandomBot();
        RandomBot secondRandomBot = new RandomBot();
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

    private boolean executeBotMove(RandomBot botService, int botNumber, BoardLogic boardLogic, GameLogic gameLogic) {
        Callable<Boolean> botMoveTask = () -> botService.makeMove(botNumber, boardLogic);
        Future<Boolean> futureMove = scheduler.schedule(botMoveTask, 0, TimeUnit.SECONDS);

        try {
            if (futureMove.get(5, TimeUnit.SECONDS)) {
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

    private void saveResultsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        File file = new File("game_results.json");

        List<Results> resultsList;

        if (file.exists()) {
            try {
                resultsList = mapper.readValue(file, new TypeReference<List<Results>>() {});
            } catch (IOException e) {
                logger.error("Error reading existing game results from JSON", e);
                resultsList = new ArrayList<>();
            }
        } else {
            resultsList = new ArrayList<>();
        }

        Results newResults = new Results(gameCount, firstBotWins.get(), secondBotWins.get(), draws.get());
        resultsList.add(newResults);

        try {
            writer.writeValue(file, resultsList);
            logger.info("Game results saved to game_results.json");
        } catch (IOException e) {
            logger.error("Error saving game results to JSON", e);
        }
    }

    private static class Results {
        public int totalGames;
        public int firstBotWins;
        public int secondBotWins;
        public int draws;

        public Results() {}

        public Results(int totalGames, int firstBotWins, int secondBotWins, int draws) {
            this.totalGames = totalGames;
            this.firstBotWins = firstBotWins;
            this.secondBotWins = secondBotWins;
            this.draws = draws;
        }

        public int getTotalGames() {
            return totalGames;
        }

        public void setTotalGames(int totalGames) {
            this.totalGames = totalGames;
        }

        public int getFirstBotWins() {
            return firstBotWins;
        }

        public void setFirstBotWins(int firstBotWins) {
            this.firstBotWins = firstBotWins;
        }

        public int getSecondBotWins() {
            return secondBotWins;
        }

        public void setSecondBotWins(int secondBotWins) {
            this.secondBotWins = secondBotWins;
        }

        public int getDraws() {
            return draws;
        }

        public void setDraws(int draws) {
            this.draws = draws;
        }
    }
}
