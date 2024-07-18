package io.deeplay.camp;

import config.LoadServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;
import java.util.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String serverIp;
    private static int serverPort;
    private static int maxLengthQueue = 50;
    
    public static void main(String[] args) throws IOException {
        var properties = LoadServerProperties.loadConfig();
        
        if (!Objects.equals(properties.getProperty("server.port"), "bad") && !Objects.equals(properties.getProperty("server.ip"), "bad")) {
            serverIp = properties.getProperty("server.ip");
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
            
            ServerSocket serverSocket = new ServerSocket(serverPort, maxLengthQueue, InetAddress.getByName(serverIp));
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread().start();
            }
        } 
    }
}