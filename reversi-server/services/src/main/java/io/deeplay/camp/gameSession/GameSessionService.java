package io.deeplay.camp.gameSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.camp.entity.GameSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GameSessionService {
    private static final String BASE_URL = "http://localhost:8083/api/game-sessions";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void addGameSession(GameSession gameSession) throws IOException {
        String json = objectMapper.writeValueAsString(new GameSessionRequest(gameSession));
        sendRequest(BASE_URL, "POST", json);
    }

    public void updateGameSession(GameSession gameSession, String log) throws IOException {
        String json = objectMapper.writeValueAsString(new GameSessionUpdateRequest(gameSession, log));
        sendRequest(BASE_URL + "/update", "PUT", json);
    }

    public GameSession getGameSessionById(int id) throws IOException {
        String response = sendRequest(BASE_URL + "/" + id, "GET", null);
        return objectMapper.readValue(response, GameSession.class);
    }

    public List<GameSession> getAllGameSessions() throws IOException {
        String response = sendRequest(BASE_URL, "GET", null);
        return objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, GameSession.class));
    }

    public void deleteGameSession(int id) throws IOException {
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

    private String convertLogToString(List<String> log) {
        return String.join("\n", log);
    }

    private static class GameSessionRequest {
        private int player1;
        private int player2;
        private String result;
        private List<String> log;
        private List<String> sessionChat;

        public GameSessionRequest(GameSession gameSession) {
            player1 = gameSession.getPlayer1().getId();
            player2 = gameSession.getPlayer2().getId();
            result = gameSession.getResult();
            log = gameSession.getLog();
            sessionChat = gameSession.getSessionChat().stream().map(Object::toString).toList();
        }
    }

    private static class GameSessionUpdateRequest {
        private GameSession gameSession;
        private String log;

        public GameSessionUpdateRequest(GameSession gameSession, String log) {
            this.gameSession = gameSession;
            this.log = log;
        }
    }
}