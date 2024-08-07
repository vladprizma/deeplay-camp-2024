package io.deeplay.camp;

import action.Action;
import board.MoveModel;
import chat.ChatModel;
import board.BoardModel;
import login.LoginModel;
import login.StartGameModel;
import login.StartSessionModel;

import java.io.IOException;

public class ModelManager {

    private LoginModel loginModel;
    private StartSessionModel startSessionModel;
    private StartGameModel startGameModel;
    private ChatModel chatModel;
    private BoardModel boardModel;
    private MoveModel moveModel;
    private Action action;
    private String loginAndPassword;
    private String chatMessages;
    private String move;

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
        boardModel = new BoardModel(action);
    }

    public void moveModelMethod(String move){
        this.move = move;
        moveModel = new MoveModel(action, move);
    }
}