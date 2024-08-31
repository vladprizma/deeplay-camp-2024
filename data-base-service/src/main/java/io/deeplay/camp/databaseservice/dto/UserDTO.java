package io.deeplay.camp.databaseservice.dto;

public class UserDTO {
    private int id;
    private String username;
    private String userPhoto;
    private int rating;
    private int matches;
    private String password;

    public UserDTO() {}

    public UserDTO(int id, String username, String userPhoto, int rating, int matches, String password) {
        this.id = id;
        this.username = username;
        this.userPhoto = userPhoto;
        this.rating = rating;
        this.matches = matches;
        this.password = password;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }
}
