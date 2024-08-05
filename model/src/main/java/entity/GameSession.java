package entity;

import enums.GameStatus;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private int currentPlayerId;
    private Board board;
    private User player1;
    private User player2;
    private GameStatus gameState = GameStatus.NOT_STARTED;
    private int sessionId;
    private String result;
    private String log;
    private List<SessionMessage> sessionChat;
    
    public GameSession() {
        sessionChat = new ArrayList<>();
    }

    public GameSession(int id, User player1, String result, User player2, String log) {
        this.sessionId = id;
        this.result = result;
        this.player1 = player1;
        this.player2 = player2;
        sessionChat = new ArrayList<>();
    }

    public int getPlayersCount() {
        int count = 0;
        if (player1 != null) count++;
        if (player2 != null) count++;
        return count;
    }
    
    public String getLog() { return log; }
    
    public void setLog(String log) { this.log = log; }
    
    public synchronized GameStatus getGameState() {
        return gameState;
    }

    public synchronized void setGameState(GameStatus gameState) {
        this.gameState = gameState;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public synchronized void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
    
    public synchronized void setResult(String result) { this.result = result; }
    
    public String getResult() { return result; }

    public Board getBoard() {
        return board;
    }

    public synchronized void setBoard(Board board) {
        this.board = board;
    }

    public User getPlayer1() {
        return player1;
    }

    public synchronized void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public synchronized void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public synchronized int getSessionId() {
        return sessionId;
    }

    public synchronized void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public List<SessionMessage> getSessionChat() {
        return sessionChat;
    }

    public synchronized void setSessionChat(List<SessionMessage> sessionChat) {
        this.sessionChat = sessionChat;
    }

    public synchronized void addMessage(SessionMessage sessionMessage) {
        this.sessionChat.add(sessionMessage);
    }
}
