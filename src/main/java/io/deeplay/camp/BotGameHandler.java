package io.deeplay.camp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class BotGameHandler {
    private static final Logger logger = LoggerFactory.getLogger(BotGameHandler.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int gameCount;

    public BotGameHandler(int gameCount) {
        this.gameCount = gameCount;
    }
    
    public void startBotGame() {
        while (true) {
            Callable<String> botMoveTask = () -> {
                return getBotMove();
            };

            Future<String> futureMove = scheduler.schedule(botMoveTask, 0, TimeUnit.SECONDS);

            try {
                String move = futureMove.get(5, TimeUnit.SECONDS);
                logger.info("Bot made a move: " + move);
            } catch (TimeoutException e) {
                logger.error("Bot move timed out.");
            } catch (Exception e) {
                logger.error("Error during bot move", e);
            }
        }
    }

    private static String getBotMove() {
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Move";
    }
}