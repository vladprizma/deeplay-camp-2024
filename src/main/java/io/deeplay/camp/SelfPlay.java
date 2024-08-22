package io.deeplay.camp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.deeplay.camp.bot.DarlingBot;
import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.bot.BotStrategy;
import io.deeplay.camp.bot.RandomBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles self-play games between bots.
 * <p>
 * This class manages the execution of multiple games between two bots, collects the results, and saves them to a JSON file.
 * It uses a scheduled executor service to manage the game threads and a fixed thread pool for game execution.
 * </p>
 */
public class SelfPlay {
    private static final Logger logger = LoggerFactory.getLogger(SelfPlay.class);
    private static final int GAME_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 10;
    private static final int SCHEDULER_THREAD_COUNT = GAME_THREAD_COUNT;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_THREAD_COUNT);
    private static final ExecutorService gameExecutor = Executors.newFixedThreadPool(GAME_THREAD_COUNT);
    private final int gameCount;
    private final AtomicInteger firstBotWins = new AtomicInteger(0);
    private final AtomicInteger secondBotWins = new AtomicInteger(0);
    private final AtomicInteger draws = new AtomicInteger(0);
    private final AtomicInteger totalGamesCompleted = new AtomicInteger(0);

    /**
     * Initializes a new SelfPlay instance with the specified number of games.
     *
     * @param gameCount The total number of games to be played.
     */
    public SelfPlay(int gameCount) {
        this.gameCount = gameCount;
    }

    /**
     * Starts the self-play games between the bots.
     * <p>
     * This method divides the total games into batches, executes them, and logs the results after each batch.
     * </p>
     */
    public void startBotGame() {
        int totalBatches = (int) Math.ceil((double) gameCount / 50);
        int index = 0;

        for (int batch = 0; batch < totalBatches; batch++) {
            int gamesInBatch = Math.min(50, gameCount - batch * 50);
            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < gamesInBatch; i++) {
                int gameIndex = batch * 10 + i;
                int finalIndex = index;
                futures.add(gameExecutor.submit(() -> playSingleGame(finalIndex % 2 == 0)));
                index++;
            }

            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.toString());
                }
            }

            logger.info("Batch {} completed. Total games: {}. Bot 1 wins: {}. Bot 2 wins: {}. Draws: {}", batch + 1, totalGamesCompleted.get(), firstBotWins.get(), secondBotWins.get(), draws.get());
        }

        logger.info("Total games: {}. Bot 1 wins: {}. Bot 2 wins: {}. Draws: {}", gameCount, firstBotWins.get(), secondBotWins.get(), draws.get());
        gameExecutor.shutdown();
        scheduler.shutdown();

        saveResultsToJson();
    }

    /**
     * Plays a single game between the bots.
     * <p>
     * This method initializes the bots and the board, executes the game, and updates the win/draw counters.
     * </p>
     *
     * @param firstBotStarts True if the first bot starts the game, false otherwise.
     * @return null
     */
    private Void playSingleGame(boolean firstBotStarts) {
        BotStrategy firstRandomBot = new DarlingBot(1, "DarlingBot", 3);
        BotStrategy secondRandomBot = new RandomBot(2, "ViolaBot");
        Board board = new Board();
        BoardService boardLogic = new BoardService(board);
        BotStrategy currentBot = firstBotStarts ? firstRandomBot : secondRandomBot;

        while (!boardLogic.checkForWin().isGameFinished()) {
            executeBotMove(currentBot, boardLogic);
            currentBot = currentBot.id == firstRandomBot.id ? secondRandomBot : firstRandomBot;
        }

        if (boardLogic.checkForWin().getUserIdWinner() == 1) {
            firstBotWins.incrementAndGet();
        } else if (boardLogic.checkForWin().getUserIdWinner() == 2) {
            secondBotWins.incrementAndGet();
        } else {
            draws.incrementAndGet();
        }

        totalGamesCompleted.incrementAndGet();
        return null;
    }

    /**
     * Executes a move for the given bot.
     * <p>
     * This method schedules the bot's move and updates the board with the move.
     * </p>
     *
     * @param botService The bot making the move.
     * @param boardLogic The board logic to be used for making the move.
     */
    private void executeBotMove(BotStrategy botService, BoardService boardLogic) {
        Callable<Tile> botMoveTask = () -> botService.getMove(botService.id, boardLogic);
        Future<Tile> futureMove = scheduler.schedule(botMoveTask, 0, TimeUnit.SECONDS);

        try {
            var tile = futureMove.get(5, TimeUnit.SECONDS);
            if (tile != null) boardLogic.makeMove(botService.id, tile);
        } catch (TimeoutException e) {
            logger.error("Bot {} move timed out.", botService.id);
            gameFinished(botService.id);
        } catch (Exception e) {
            logger.error("Error during bot {} move", botService.id, e);
            logger.error(e.toString());
            gameFinished(botService.id);
        }
    }

    /**
     * Updates the game results when a bot loses.
     * <p>
     * This method increments the win counter for the other bot and the total games completed counter.
     * </p>
     *
     * @param botNumberLose The ID of the bot that lost.
     */
    private void gameFinished(int botNumberLose) {
        if (botNumberLose == 1) {
            secondBotWins.incrementAndGet();
        } else {
            firstBotWins.incrementAndGet();
        }
        totalGamesCompleted.incrementAndGet();
    }

    /**
     * Saves the game results to a JSON file.
     * <p>
     * This method reads existing results from the file, adds the new results, and writes them back to the file.
     * </p>
     */
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

    /**
     * Represents the results of the self-play games.
     * <p>
     * This class holds the total number of games, the number of wins for each bot, and the number of draws.
     * </p>
     */
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