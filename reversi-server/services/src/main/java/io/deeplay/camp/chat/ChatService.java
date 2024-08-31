package io.deeplay.camp.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.camp.dto.ChatMessageRequest;
import io.deeplay.camp.entity.ChatMessage;
import io.deeplay.camp.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Service for handling chat message operations.
 * <p>
 * This class provides methods for adding, retrieving, and deleting chat messages.
 * It communicates with the chat microservice using HTTP requests.
 * </p>
 */
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static final String BASE_URL = "http://localhost:8083/api/chat-messages"; // URL вашего микросервиса
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Adds a new chat message to the microservice.
     * <p>
     * This method creates a new ChatMessage object with the given user and message, sets the current timestamp,
     * and sends it to the microservice using HTTP POST.
     * </p>
     *
     * @param user    The user who sent the message.
     * @param message The content of the message.
     * @throws IOException If an IO error occurs during the operation.
     */
    public void addMessage(User user, String message) throws IOException {
        logger.info("Adding new message from user: {}", user.getUsername());
        if (message == null || message.trim().isEmpty()) {
            logger.warn("Message content is empty");
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        ChatMessageRequest chatMessage = new ChatMessageRequest(user.getId(), message, new java.sql.Timestamp(System.currentTimeMillis()));
        String json = objectMapper.writeValueAsString(chatMessage);
        sendRequest(BASE_URL, "POST", json);
    }

    /**
     * Retrieves all chat messages from the microservice.
     * <p>
     * This method fetches all chat messages from the microservice using HTTP GET.
     * </p>
     *
     * @return A list of all chat messages.
     * @throws IOException If an IO error occurs during the operation.
     */
    public List<ChatMessage> getAllMessages() throws IOException {
        logger.info("Retrieving all chat messages");
        String response = sendRequest(BASE_URL, "GET", null);
        return objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, ChatMessage.class));
    }

    /**
     * Deletes a chat message by its ID.
     * <p>
     * This method deletes the chat message with the given ID from the microservice using HTTP DELETE.
     * </p>
     *
     * @param id The ID of the chat message to be deleted.
     * @throws IOException If an IO error occurs during the operation.
     */
    public void deleteMessage(int id) throws IOException {
        logger.info("Deleting message with ID: {}", id);
        sendRequest(BASE_URL + "/delete/" + id, "DELETE", null);
    }

    private String sendRequest(String urlString, String method, String json) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        if (json != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to execute request. HTTP response code: " + responseCode);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
