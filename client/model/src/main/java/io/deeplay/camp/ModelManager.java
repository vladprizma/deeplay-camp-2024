package io.deeplay.camp;

import action.Action;
import chat.ChatModel;
import board.boardModel;
import login.LoginModel;
import login.StartGameModel;
import login.StartSessionModel;

import java.io.IOException;

public class ModelManager {

    private LoginModel loginModel;
    private StartSessionModel startSessionModel;
    private StartGameModel startGameModel;
    private ChatModel chatModel;
    private boardModel boardModel;
    private Action action;
    private String loginAndPassword;
    private String chatMessages;

    public ModelManager() throws IOException {
        action = new Action();
    }

    public void loginModelMethod(String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        loginModel = new LoginModel(action, loginAndPassword);
    }

    public void startSessionModelMethod() {
        startSessionModel = new StartSessionModel(action);
        startSessionModel.startSessionMethod();
    }

    public void startGameModelMethod() {
        startGameModel = new StartGameModel(action);
        startGameModel.startGameMethod();
    }

    public void chatModelMethod(String chatMessages) {
        this.chatMessages = chatMessages;
        chatModel = new ChatModel(action, chatMessages);
    }

    public void boardModelMethod() {
        boardModel = new boardModel(action);
    }
}