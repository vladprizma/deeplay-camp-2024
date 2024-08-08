package io.deeplay.camp.repository;

import io.deeplay.camp.handlers.main.MainHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Interface for handling commands in the game.
 * <p>
 * This interface defines a method for handling commands received from the client. Implementations of this interface
 * should provide the logic for processing specific commands. The method can throw various exceptions that need to be
 * handled by the caller.
 * </p>
 */
public interface CommandHandler {

    /**
     * Handles a command received from the client.
     * <p>
     * This method processes the given command message and performs the necessary actions using the provided
     * MainHandler. It can throw IOException, SQLException, or InterruptedException, which should be handled
     * appropriately by the caller.
     * </p>
     *
     * @param message     The command message received from the client.
     * @param mainHandler The handler managing the client connection and game context.
     * @throws IOException          If an I/O error occurs during command processing.
     * @throws SQLException         If a SQL error occurs during command processing.
     * @throws InterruptedException If the thread is interrupted during command processing.
     */
    void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException;
}