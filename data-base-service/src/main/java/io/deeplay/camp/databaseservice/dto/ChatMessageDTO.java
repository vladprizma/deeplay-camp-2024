package io.deeplay.camp.databaseservice.dto;

import io.deeplay.camp.databaseservice.model.User;

import java.sql.Timestamp;

public class ChatMessageDTO {
    private int id;
    private User userId;
    private String message;
    private Timestamp timestamp;

    public ChatMessageDTO() {}

    public ChatMessageDTO(int id, User userId, String message, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
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
