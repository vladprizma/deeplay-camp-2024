package board;

import action.Action;

public class MoveModel {

    private String move;
    private Action action;

    public MoveModel(Action action, String move) {
        this.move = move;
        this.action = action;
        moveModelMethod();
    }

    private void moveModelMethod() {
        action.handleMoveAction(move);
    }
}
