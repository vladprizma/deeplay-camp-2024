package io.deeplay.camp.entity;

/**
 * Represents a user in the system.
 * <p>
 * This class holds the details of a user, including the ID, username, password, rating, matches, user photo, ELO rating, and whether the user is a bot.
 * It provides methods to get and set these details.
 * </p>
 */
public class User {
    private int id;
    private String username;
    private String password;
    private int rating;
    private int matches;
    private String userPhoto;
    private int elo;
    private boolean isBot;

    /**
     * Initializes a new User with the specified details.
     *
     * @param id        The ID of the user.
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param rating    The rating of the user.
     * @param matches   The number of matches played by the user.
     * @param userPhoto The photo of the user.
     */
    public User(int id, String username, String password, int rating, int matches, String userPhoto) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.matches = matches;
        this.userPhoto = userPhoto;
        isBot = false;
    }

    /**
     * Gets the ID of the user.
     *
     * @return The ID of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id The new ID of the user.
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
     * @param username The new username of the user.
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
     * @param password The new password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @param rating The new rating of the user.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the number of matches played by the user.
     *
     * @return The number of matches played by the user.
     */
    public int getMatches() {
        return matches;
    }

    /**
     * Sets the number of matches played by the user.
     *
     * @param matches The new number of matches played by the user.
     */
    public void setMatches(int matches) {
        this.matches = matches;
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
     * @param userPhoto The new photo of the user.
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * Gets the ELO rating of the user.
     *
     * @return The ELO rating of the user.
     */
    public int getElo() {
        return elo;
    }

    /**
     * Sets the ELO rating of the user.
     *
     * @param elo The new ELO rating of the user.
     */
    public void setElo(int elo) {
        this.elo = elo;
    }

    /**
     * Gets whether the user is a bot.
     *
     * @return True if the user is a bot, false otherwise.
     */
    public boolean getIsBot() {
        return isBot;
    }

    /**
     * Sets whether the user is a bot.
     *
     * @param isBot The new bot status of the user.
     */
    public void setIsBot(boolean isBot) {
        this.isBot = isBot;
    }
}