package io.deeplay.camp.databaseservice.model;

import jakarta.persistence.*;

/**
 * Entity class representing a user.
 */
@Entity
@Table(name = "users")
public class User {
    
    /**
     * The unique identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The username of the user.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The photo of the user.
     */
    @Column(name = "user_photo", nullable = false)
    private String userPhoto;

    /**
     * The rating of the user.
     */
    @Column(name = "rating", nullable = false)
    private int rating;

    /**
     * The number of matches the user has participated in.
     */
    @Column(name = "matches", nullable = false)
    private int matches;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructs a new User with the specified details.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param userPhoto The photo of the user.
     * @param rating The rating of the user.
     * @param matches The number of matches the user has participated in.
     */
    public User(String username, String password, String userPhoto, int rating, int matches) {
        this.username = username;
        this.password = password;
        this.userPhoto = userPhoto;
        this.rating = rating;
        this.matches = matches;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The unique identifier of the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the photo of the user.
     *
     * @return The photo of the user.
     */
    public String getUserPhoto() {
        return userPhoto;
    }

    /**
     * Sets the photo of the user.
     *
     * @param userPhoto The photo of the user.
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * Gets the rating of the user.
     *
     * @return The rating of the user.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating of the user.
     *
     * @param rating The rating of the user.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the number of matches the user has participated in.
     *
     * @return The number of matches the user has participated in.
     */
    public int getMatches() {
        return matches;
    }

    /**
     * Sets the number of matches the user has participated in.
     *
     * @param matches The number of matches the user has participated in.
     */
    public void setMatches(int matches) {
        this.matches = matches;
    }
}