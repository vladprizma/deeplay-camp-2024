package action;

import sigletonobserver.ChatString;
import client.Client;
import client.ClientManager;
import client.TCPClient;
import commands.request.*;
import tokens.TokenStorage;

import java.io.IOException;

public class Action {
    private Client client;
    public static final TokenStorage tokenStorage = new TokenStorage();

    public Action() throws IOException {
        client = ClientManager.getClient();
        ((TCPClient)client).setAction(this);
    }

    public void handleMoveAction(int x, int y) {
        try {
            CommandRequest moveCommandRequest = new MoveCommandRequest(client, x, y);
            moveCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStartAction() {
        try {
            CommandRequest startCommandRequest = new StartCommandRequest(client);
            startCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePauseAction() {
        try {
            CommandRequest pauseCommandRequest = new PauseCommandRequest(client);
            pauseCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResumeAction() {
        try {
            CommandRequest resumeCommandRequest = new ResumeCommandRequest(client);
            resumeCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStartSessionAction() {
        try {
            CommandRequest startSessionCommandRequest = new StartSessionCommandRequest(client, tokenStorage.getUpdateToken());
            startSessionCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLoginAction(String loginAndPassword) {
        try {
            System.out.println(tokenStorage.getRefreshToken());
            CommandRequest loginCommandRequest = new LoginCommandRequest(client, loginAndPassword);
            loginCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLoginActionResponse(String refreshToken, String updateToken) {
        tokenStorage.saveTokens(refreshToken, updateToken);
        handleStartSessionAction();
    }

    public void handleChatAction(String chatMessages) {
        try {
            CommandRequest chatCommandRequest = new ChatCommandRequest(client, chatMessages);
            chatCommandRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleChatActionResponse(String chatMessages) {
        ChatString singleton = ChatString.getInstance();
        singleton.setString(chatMessages);
    }
}