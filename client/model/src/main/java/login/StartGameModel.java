package login;

import action.Action;

public class StartGameModel {
    private Action action;

    public StartGameModel(Action action) {
        this.action = action;
    }

    public void startGameMethod() {
        action.handleStartAction();
    }
}
