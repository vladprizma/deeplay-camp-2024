package login;

import action.Action;

public class BotStartGameModel {
    private Action action;

    public BotStartGameModel(Action action) {
        this.action = action;
    }

    public void startBotGameMethod() {
        action.handleBotStartAction();
    }
}
