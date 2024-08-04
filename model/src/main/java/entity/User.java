package entity;

import enums.Color;

public class User {
    private int id;
    private String username;
    private String password;
    private int rating;
    private int matches;
    private String userPhoto;
    private int elo;

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
    
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    
    public void setPassword(String password) { this.password = password; }
    
    public int getRating() { return rating; }
    
    public int getMatches() { return matches; }
    
    public void setRating(int rating) { this.rating = rating; }
    
    public void setMatches(int matches) { this.matches = matches; }
    
    public String getUserPhoto() { return this.userPhoto; }
    
    public void setUserPhoto(String userPhoto) { this.userPhoto = userPhoto; }
    
    public void setElo(int elo) { this.elo = elo; }
    
    public int getElo() { return elo; }
}
