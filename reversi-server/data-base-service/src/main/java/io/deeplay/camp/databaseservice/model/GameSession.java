package io.deeplay.camp.databaseservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "gamesessions")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    @Column(nullable = false)
    private String result;

    @ElementCollection
    @CollectionTable(name = "gamesession_log", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "log_entry")
    private List<String> log;

    @ElementCollection
    @CollectionTable(name = "gamesession_chat", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "chat_entry")
    private List<String> sessionChat;

    public GameSession() {}

    public GameSession(User player1, User player2, String result, List<String> log, List<String> sessionChat) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.log = log;
        this.sessionChat = sessionChat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public List<String> getSessionChat() {
        return sessionChat;
    }

    public void setSessionChat(List<String> sessionChat) {
        this.sessionChat = sessionChat;
    }
}
