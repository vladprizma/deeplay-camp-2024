package io.deeplay.camp;

import action.Action;
import chat.ChatModel;
import board.boardModel;
import login.*;

import java.io.IOException;

public class ModelManager {

    private LoginModel loginModel;
    private RegisterModel registerModel;
    private StartSessionModel startSessionModel;
    private StartGameModel startGameModel;
    private BotStartGameModel botStartGameModel;
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

    public void registerModelMethod(String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        registerModel = new RegisterModel(action, loginAndPassword);
    }

    public void startSessionModelMethod() {
        startSessionModel = new StartSessionModel(action);
        startSessionModel.startSessionMethod();
    }

    public void startGameModelMethod() {
        startGameModel = new StartGameModel(action);
        startGameModel.startGameMethod();
    }

    public void startBotGameModelMethod() {
        botStartGameModel = new BotStartGameModel(action);
        botStartGameModel.startBotGameMethod();
    }

    public void chatModelMethod(String chatMessages) {
        this.chatMessages = chatMessages;
        chatModel = new ChatModel(action, chatMessages);
    }

    public void boardModelMethod() {
        boardModel = new boardModel(action);
    }
}