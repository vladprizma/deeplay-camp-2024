package io.deeplay.camp.databaseservice.dto;

import java.sql.Timestamp;

public class ChatMessageRequest {
    private int userId;
    private String message;
    private Timestamp timestamp;

    public ChatMessageRequest() {}

    public ChatMessageRequest(int userId, String message, Timestamp timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}