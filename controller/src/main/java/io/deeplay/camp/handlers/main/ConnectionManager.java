package io.deeplay.camp.handlers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the connection between the server and a client.
 * <p>
 * This class is responsible for handling the input and output streams of a socket connection.
 * It provides methods to send messages to the client, retrieve the input reader, and close the connection.
 * It also logs the process and handles any unexpected errors that may occur.
 * </p>
 */
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Initializes a new ConnectionManager with the given socket.
     * <p>
     * This constructor sets up the input and output streams for the socket.
     * </p>
     *
     * @param socket The socket representing the connection to the client.
     * @throws IOException If an I/O error occurs when creating the input or output streams.
     */
    public ConnectionManager(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        logger.info("Connection established with client: {}", socket.getRemoteSocketAddress());
    }

    /**
     * Sends a message to the client.
     * <p>
     * This method sends a message to the client through the output stream.
     * </p>
     *
     * @param msg The message to be sent to the client.
     */
    public void sendMessageToClient(String msg) {
        out.println(msg);
        out.flush();
        logger.info("Sent message to client: {}", msg);
    }

    /**
     * Retrieves the input reader for the connection.
     * <p>
     * This method returns the BufferedReader for reading input from the client.
     * </p>
     *
     * @return The BufferedReader for the input stream.
     */
    public BufferedReader getInputReader() {
        return in;
    }

    /**
     * Retrieves the socket for the connection.
     * <p>
     * This method returns the socket representing the connection to the client.
     * </p>
     *
     * @return The socket for the connection.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Closes the connection to the client.
     * <p>
     * This method closes the socket and the input and output streams. It also logs the process and handles any
     * unexpected errors that may occur.
     * </p>
     */
    public void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
            logger.info("Connection closed with client: {}", socket.getRemoteSocketAddress());
        } catch (IOException e) {
            logger.error("Error closing connection with client: {}", socket.getRemoteSocketAddress(), e);
        }
    }
}