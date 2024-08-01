package entity;

public class SessionMessage {
    private String msg;
    private String username;
    
    public SessionMessage(String msg, String username) {
        this.msg = msg;
        this.username = username;
    }
    
    public String getMsg() { return msg; }
    
    public void setMsg(String msg) { this.msg = msg; }
    
    public String getUsername() { return username; }
    
    public void setUsername(String username) { this.username = username; }
}
