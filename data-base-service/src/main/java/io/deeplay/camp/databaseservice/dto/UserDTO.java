package io.deeplay.camp.databaseservice.dto;

/**
 * Data Transfer Object for user details.
 */
public class UserDTO {
    private int id;
    private String username;
    private String userPhoto;
    private int rating;
    private int matches;
    private String password;

    /**
     * Default constructor.
     */
    public UserDTO() {}

    /**
     * Constructs a new UserDTO with the specified details.
     *
     * @param id The unique identifier of the user.
     * @param username The username of the user.
     * @param userPhoto The photo of the user.
     * @param rating The rating of the user.
     * @param matches The number of matches the user has participated in.
     * @param password The password of the user.
     */
    public UserDTO(int id, String username, String userPhoto, int rating, int matches, String password) {
        this.id = id;
        this.username = username;
        this.userPhoto = userPhoto;
        this.rating = rating;
        this.matches = matches;
        this.password = password;
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
}