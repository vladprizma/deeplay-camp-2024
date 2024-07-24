package entity;

import enums.Color;

public class Player {
    private int id;
    private Color color;
    private String username;
    private String password;
    private int rating;
    private int matches;

    public Player(int id, Color color, String username, String password) {
        this.id = id;
        this.color = color;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
    
    public String getUsername() { return username; }
    
    public String getPassword() { return password; }
    
    public int getRating() { return rating; }
    
    public int getMatches() { return matches; }
    
    public void setRating(int rating) { this.rating = rating; }
    
    public void setMatches(int matches) { this.matches = matches; }
}
