package entity;

import enums.Color;

public class User {
    private int id;
    private String username;
    private String password;
    private int rating;
    private int matches;
    private String userPhoto;

    public User(int id, String username, String password, int rating, int matches, String userPhoto) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.matches = matches;
        this.userPhoto = userPhoto;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    
    public String getPassword() { return password; }
    
    public int getRating() { return rating; }
    
    public int getMatches() { return matches; }
    
    public void setRating(int rating) { this.rating = rating; }
    
    public void setMatches(int matches) { this.matches = matches; }
    
    public String getUserPhoto() { return this.userPhoto; }
}
