package login;

import action.Action;

public class LoginModel {

    private String loginAndPassword;
    private Action action;

    public LoginModel(Action action, String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        this.action = action;
        loginModelMethod();
    }

    private void loginModelMethod() {
        action.handleLoginAction(loginAndPassword);
    }
}
