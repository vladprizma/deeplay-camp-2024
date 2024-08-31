package io.deeplay.camp.databaseservice.dto;

/**
 * Data Transfer Object for tokens.
 */
public class TokenDTO {
    private int userId;
    private String refreshToken;
    private String updateToken;

    /**
     * Default constructor.
     */
    public TokenDTO() {}

    /**
     * Constructs a new TokenDTO with the specified details.
     *
     * @param userId The ID of the user associated with the token.
     * @param refreshToken The refresh token string.
     * @param updateToken The update token string.
     */
    public TokenDTO(int userId, String refreshToken, String updateToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
    }

    /**
     * Gets the ID of the user associated with the token.
     *
     * @return The ID of the user associated with the token.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with the token.
     *
     * @param userId The ID of the user associated with the token.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the refresh token string.
     *
     * @return The refresh token string.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token string.
     *
     * @param refreshToken The refresh token string.
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets the update token string.
     *
     * @return The update token string.
     */
    public String getUpdateToken() {
        return updateToken;
    }

    /**
     * Sets the update token string.
     *
     * @param updateToken The update token string.
     */
    public void setUpdateToken(String updateToken) {
        this.updateToken = updateToken;
    }
}