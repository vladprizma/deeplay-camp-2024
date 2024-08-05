package login;

import action.Action;

public class StartSessionModel {
    private Action action;

    public StartSessionModel(Action action) {
        this.action = action;
    }

    public void startSessionMethod() {
        action.handleStartSessionAction();
    }
}
