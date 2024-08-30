package io.deeplay.camp.databaseservice.dto;

public class TokenDTO {
    private int id;
    private int userId;
    private String refreshToken;
    private String updateToken;

    public TokenDTO() {}

    public TokenDTO(int id, int userId, String refreshToken, String updateToken) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUpdateToken() {
        return updateToken;
    }

    public void setUpdateToken(String updateToken) {
        this.updateToken = updateToken;
    }
}
