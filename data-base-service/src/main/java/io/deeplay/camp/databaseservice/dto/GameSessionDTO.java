package io.deeplay.camp.databaseservice.dto;

import java.util.List;

public class GameSessionDTO {
    private int id;
    private int player1Id;
    private int player2Id;
    private String result;
    private List<String> log;
    private List<String> sessionChat;

    public GameSessionDTO() {}

    public GameSessionDTO(int id, int player1Id, int player2Id, String result, List<String> log, List<String> sessionChat) {
        this.id = id;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.result = result;
        this.log = log;
        this.sessionChat = sessionChat;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
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
