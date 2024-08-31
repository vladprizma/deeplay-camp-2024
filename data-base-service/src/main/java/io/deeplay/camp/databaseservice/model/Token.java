package io.deeplay.camp.databaseservice.model;

import io.deeplay.camp.databaseservice.model.User;
import jakarta.persistence.*;

/**
 * Entity class representing a token.
 */
@Entity
@Table(name = "tokens")
public class Token {

    /**
     * The unique identifier of the token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The user associated with the token.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * The refresh token string.
     */
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    /**
     * The update token string.
     */
    @Column(name = "update_token", nullable = false)
    private String updateToken;

    /**
     * Default constructor.
     */
    public Token() {}

    /**
     * Constructs a new Token with the specified details.
     *
     * @param user The user associated with the token.
     * @param refreshToken The refresh token string.
     * @param updateToken The update token string.
     */
    public Token(User user, String refreshToken, String updateToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.updateToken = updateToken;
    }

    /**
     * Gets the unique identifier of the token.
     *
     * @return The unique identifier of the token.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the token.
     *
     * @param id The unique identifier of the token.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user associated with the token.
     *
     * @return The user associated with the token.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the token.
     *
     * @param user The user associated with the token.
     */
    public void setUser(User user) {
        this.user = user;
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