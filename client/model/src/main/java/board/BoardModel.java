package board;

import action.Action;

public class BoardModel {
    private Action action;

    public BoardModel(Action action) {
        this.action = action;
        boardModelMethod();
    }

    private void boardModelMethod() {
        action.handleBoardActionRequest();
    }
}
