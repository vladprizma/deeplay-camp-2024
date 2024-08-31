package io.deeplay.camp.databaseservice.dto;

public class TokenDTO {
    private int userId;
    private String refreshToken;
    private String updateToken;

    public TokenDTO() {}

    public TokenDTO(int userId, String refreshToken, String updateToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
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
