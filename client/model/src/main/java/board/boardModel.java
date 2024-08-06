package board;

import action.Action;

public class boardModel {
    private Action action;

    public boardModel(Action action) {
        this.action = action;
        boardModelMethod();
    }

    private void boardModelMethod() {
        action.handleBoardActionRequest();
    }
}
