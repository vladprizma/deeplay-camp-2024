package io.deeplay.camp.token;

public class TokensRequest {
    public String refreshToken;
    public String updateToken;

    public TokensRequest(String refreshToken, String updateToken) {
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
    }
}
