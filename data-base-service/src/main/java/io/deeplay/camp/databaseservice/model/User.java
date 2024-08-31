package io.deeplay.camp.databaseservice.model;

import jakarta.persistence.*;
import org.aspectj.weaver.ast.Var;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @Column(name = "username", nullable = false, unique = true)
    String username;
    
    @Column(name = "password", nullable = false)
    String password;
    
    @Column(name = "user_photo", nullable = false)
    String userPhoto;
    
    @Column(name = "rating", nullable = false)
    int rating;
    
    @Column(name = "matches", nullable = false)
    int matches;

    public User() {}

    public User(String username, String password, String userPhoto, int rating, int matches) {
        this.username = username;
        this.password = password;
        this.userPhoto = userPhoto;
        this.rating = rating;
        this.matches = matches;
    }

    public int getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
