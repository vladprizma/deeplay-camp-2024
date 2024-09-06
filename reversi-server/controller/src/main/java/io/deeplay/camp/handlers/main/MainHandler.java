package io.deeplay.camp.handlers.main;

import com.sun.management.OperatingSystemMXBean;
import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.Main;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.game.GameService;
import io.deeplay.camp.handlers.commands.*;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.metrics.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MainHandler is responsible for managing the main operations of a game session.
 * <p>
 * This class handles the connection with the client, dispatches commands to their respective handlers,
 * and manages the game context. It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class MainHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final ConnectionManager connectionManager;
    private final GameContext gameContext;
    private final CommandDispatcher commandDispatcher;
    private final MetricsService metricsService;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final AtomicInteger gameSessionsCount = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final HardwareAbstractionLayer hal = systemInfo.getHardware();
    
    public static String splitRegex = " ";

    /**
     * Initializes a new MainHandler with the given socket.
     * <p>
     * This constructor sets up the connection manager, game context, and command dispatcher.
     * It also registers all the command handlers.
     * </p>
     *
     * @param socket The socket representing the connection to the client.
     * @throws IOException If an I/O error occurs when creating the input or output streams.
     */
    public MainHandler(Socket socket, MetricsService metricsService) throws IOException {
        this.connectionManager = new ConnectionManager(socket);
        this.metricsService = metricsService;
        this.gameContext = new GameContext();
        this.commandDispatcher = new CommandDispatcher();

        registerCommandHandlers();
        startThroughputReporting();
        startErrorRateReporting();
        startGameSessionsCountReporting();
        startServerLoadReporting();
    }

    /**
     * Registers all the command handlers.
     * <p>
     * This method adds all the command handlers to the command dispatcher.
     * </p>
     */
    private void registerCommandHandlers() {
        commandDispatcher.registerCommandHandler("login", new LoginCommandHandler());
        commandDispatcher.registerCommandHandler("register", new RegisterCommandHandler());
        commandDispatcher.registerCommandHandler("game-start", new StartCommandHandler());
        commandDispatcher.registerCommandHandler("session-start", new SessionStartCommandHandler());
        commandDispatcher.registerCommandHandler("send-global-message", new SendMessageCommandHandler());
        commandDispatcher.registerCommandHandler("get-global-messages", new GetMessagesCommandHandler());
        commandDispatcher.registerCommandHandler("game-pause", new PauseCommandHandler());
        commandDispatcher.registerCommandHandler("game-disconnect", new DisconnectCommandHandler());
        commandDispatcher.registerCommandHandler("game-move", new MoveCommandHandler());
        commandDispatcher.registerCommandHandler("send-message-session-chat", new SendSessionChatCommandHandler());
        commandDispatcher.registerCommandHandler("get-messages-session-chat", new GetSessionChatCommandHandler());
        commandDispatcher.registerCommandHandler("get-board", new GetBoardCommandHandler());
        commandDispatcher.registerCommandHandler("get-valid-moves", new GetValidMovesCommandHandler());
        commandDispatcher.registerCommandHandler("update-user-profile", new UpdateProfileCommandHandler());
    }

    /**
     * The main run method for the handler.
     * <p>
     * This method waits for messages from the client and dispatches them to the appropriate command handler.
     * It also logs the process and handles any unexpected errors that may occur.
     * </p>
     */
    @Override
    public void run() {
        try {
            SessionManager.getInstance().addHandler(this);

            logger.info("Waiting for session.");
            connectionManager.sendMessageToClient("Waiting for session.");

            String message;

            while ((message = connectionManager.getInputReader().readLine()) != null) {
                long startTime = System.currentTimeMillis();
                try {
                    commandDispatcher.dispatchCommand(message, this);
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    throw e;
                } finally {
                    long endTime = System.currentTimeMillis();
                    long responseTime = endTime - startTime;

                    requestCount.incrementAndGet();
                    metricsService.insertResponseTime(responseTime, SessionManager.getInstance().getHandlers().size(), 1);
                    logger.info("Response time: {}", responseTime);
                }
            }
        } catch (IOException | SQLException | InterruptedException e) {
            errorCount.incrementAndGet();
            logger.error("Error in MainHandler run method", e);
        } catch (Exception e) {
            errorCount.incrementAndGet();
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Starts the throughput reporting task.
     * <p>
     * This method schedules a task to report the throughput every minute.
     * </p>
     */
    private void startThroughputReporting() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                int throughput = requestCount.getAndSet(0);
                metricsService.insertThroughput(throughput, 1);
                logger.info("Throughput: {}", throughput);
            } catch (Exception e) {
                logger.error("Error reporting throughput", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Starts the error rate reporting task.
     * <p>
     * This method schedules a task to report the error rate every minute.
     * </p>
     */
    private void startErrorRateReporting() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                int totalRequests = requestCount.get();
                int errors = errorCount.getAndSet(0);
                double errorRate = totalRequests == 0 ? 0 : (double) errors / totalRequests;
                metricsService.insertErrorRate(errorRate, 1);
                logger.info("Error rate: {}", errorRate);
            } catch (Exception e) {
                logger.error("Error reporting error rate", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Starts the game sessions count reporting task.
     * <p>
     * This method schedules a task to report the count of game sessions every minute.
     * </p>
     */
    private void startGameSessionsCountReporting() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                gameSessionsCount.set(SessionManager.getInstance().getSessions().size());
                int sessionsCount = gameSessionsCount.get();
                metricsService.insertGameSessionsCount(sessionsCount, 1);
                logger.info("Game sessions count: {}", sessionsCount);
            } catch (Exception e) {
                logger.error("Error reporting game sessions count", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Starts the server load reporting task.
     * <p>
     * This method schedules a task to report the server load every minute.
     * </p>
     */
    private void startServerLoadReporting() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                double cpuUsage = getCpuUsage();
                double memoryUsage = getMemoryUsage();
                double diskUsage = getDiskUsage();
                metricsService.insertServerLoad(cpuUsage, memoryUsage, diskUsage, 1);
                logger.info("Server load - CPU: {}%, Memory: {}%, Disk: {}%", cpuUsage, memoryUsage, diskUsage);
            } catch (Exception e) {
                logger.error("Error reporting server load", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * Retrieves the current CPU usage.
     *
     * @return The current CPU usage as a percentage.
     */
    private double getCpuUsage() {
        com.sun.management.OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
        return osBean.getProcessCpuLoad() * 100;
    }

    /**
     * Retrieves the current memory usage.
     *
     * @return The current memory usage as a percentage.
     */
    private double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        return ((double) usedMemory / runtime.maxMemory()) * 100;
    }

    /**
     * Retrieves the current disk usage.
     *
     * @return The current disk usage as a percentage.
     */
    private double getDiskUsage() {
        return (1.0);
    }

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    public User getUser() {
        return gameContext.getUser();
    }

    /**
     * Sets the current user.
     *
     * @param user The user to be set.
     */
    public void setUser(User user) {
        gameContext.setUser(user);
        logger.info("User set: {}", user);
    }

    /**
     * Retrieves the current game session.
     *
     * @return The current game session.
     */
    public GameSession getSession() {
        return gameContext.getSession();
    }

    /**
     * Sets the current game session.
     *
     * @param session The game session to be set.
     */
    public void setSession(GameSession session) {
        gameContext.setSession(session);
        logger.info("Game session set: {}", session);
    }

    /**
     * Checks if the user is logged in.
     *
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLogin() {
        return gameContext.isLogin();
    }

    /**
     * Sets the login status of the user.
     *
     * @param login The login status to be set.
     */
    public void setLogin(boolean login) {
        gameContext.setLogin(login);
        logger.info("Login status set to: {}", login);
    }

    /**
     * Retrieves the current game logic.
     *
     * @return The current game logic.
     */
    public GameService getGameLogic() {
        return gameContext.getGameLogic();
    }

    /**
     * Sets the current game logic.
     *
     * @param gameLogic The game logic to be set.
     */
    public void setGameLogic(GameService gameLogic) {
        gameContext.setGameLogic(gameLogic);
        logger.info("Game logic set: {}", gameLogic);
    }

    /**
     * Retrieves the current board logic.
     *
     * @return The current board logic.
     */
    public BoardService getBoardLogic() {
        return gameContext.getBoardLogic();
    }

    /**
     * Sets the current board logic.
     *
     * @param boardLogic The board logic to be set.
     */
    public void setBoardLogic(BoardService boardLogic) {
        gameContext.setBoardLogic(boardLogic);
        logger.info("Board logic set: {}", boardLogic);
    }

    /**
     * Sets the board for the current game session.
     *
     * @param board The board to be set.
     */
    public void setBoard(Board board) {
        gameContext.setBoard(board);
        logger.info("Board set for session: {}", board);
    }

    /**
     * Retrieves the board for the current game session.
     *
     * @return The current board.
     */
    public Board getBoard() {
        return gameContext.getBoard();
    }

    /**
     * Sends a message to the client.
     *
     * @param msg The message to be sent to the client.
     */
    public void sendMessageToClient(String msg) {
        connectionManager.sendMessageToClient(msg);
        logger.info("Sent message to client: {}", msg);
    }

    /**
     * Retrieves the socket for the connection.
     *
     * @return The socket for the connection.
     */
    public Socket getHandlerSocket() {
        return connectionManager.getSocket();
    }

    /**
     * Closes the connection to the client.
     * <p>
     * This method closes the socket and the input and output streams. It also logs the process and handles any
     * unexpected errors that may occur.
     * </p>
     */
    public void closeConnection() {
        logger.info("Player disconnect.");
        SessionManager.getInstance().deleteHandler(this);
        connectionManager.closeConnection();
    }

    /**
     * Retrieves the logger for the handler.
     *
     * @return The logger for the handler.
     */
    public Logger getLogger() {
        return logger;
    }
}