package io.deeplay.camp.databaseservice.dto;

import java.util.List;

public class GameSessionRequest {
    private int player1;
    private int player2;
    private String result;
    private List<String> log;
    private List<String> sessionChat;
    
    public GameSessionRequest(int player1, int player2, String result, List<String> log, List<String> sessionChat) {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.log = log;
        this.sessionChat = sessionChat;
    }
    
    public GameSessionRequest() {
    }
    
    public int getPlayer1() {
        return player1;
    }
    
    public void setPlayer(int player1) {
        this.player1 = player1;
    }
    
    public int getPlayer2() {
        return player2;
    }
    
    public void setPlayer2(int player2) {
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
