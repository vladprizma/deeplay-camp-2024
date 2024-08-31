package io.deeplay.camp.entity;

/**
 * Represents a request containing refresh and update tokens.
 * <p>
 * This class holds the refresh token and the update token. It provides a constructor to initialize these tokens.
 * </p>
 */
public class TokensRequest {
    public String refreshToken;
    public String updateToken;
    public int userId;

    /**
     * Initializes a new TokensRequest with the given refresh token and update token.
     *
     * @param refreshToken The refresh token.
     * @param updateToken  The update token.
     */
    public TokensRequest(String refreshToken, String updateToken, int userId) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
    }
}