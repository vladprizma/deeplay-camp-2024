package io.deeplay.camp.repository;

import io.deeplay.camp.handlers.MainHandler;

import java.io.IOException;
import java.sql.SQLException;

public interface CommandHandler {
    void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException;
}
