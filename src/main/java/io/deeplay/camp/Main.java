package io.deeplay.camp;

import config.LoadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String serverIp;
    private static int serverPort;
    
    public static void main(String[] args) {
        var properties = LoadConfig.loadConfig();
        
        if (!Objects.equals(properties.getProperty("server.port"), "bad") && !Objects.equals(properties.getProperty("server.ip"), "bad")) {
            serverIp = properties.getProperty("server.ip");
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        }
        else logger.error("Не удалось запустить сервер. Проверьте логи, чтобы найти причину.");
    }
}