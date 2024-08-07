package login;

import action.Action;

public class RegisterModel {
    private String loginAndPassword;
    private Action action;

    public RegisterModel(Action action, String loginAndPassword) {
        this.loginAndPassword = loginAndPassword;
        this.action = action;
        registerModelMethod();
    }

    private void registerModelMethod() {
        action.handleRegistertAction(loginAndPassword);
    }
}
