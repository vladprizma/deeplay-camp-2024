package chat;

import action.Action;

public class ChatModel {

    private String loginAndPassword;
    private Action action;

    public ChatModel(Action action, String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        this.action = action;
        loginModelMethod();
    }

    private void loginModelMethod() {
        action.handleLoginAction(loginAndPassword);
    }
}
