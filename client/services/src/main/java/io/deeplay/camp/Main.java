package io.deeplay.camp;

import action.Action;

public class Main {
    public static void main(String[] args) {
        Action action = new Action();
        action.handleStartAction(2);
        action.handleMoveAction(1, 5, 6);
        action.handlePauseAction(2);
        action.handleResumeAction(2);
        action.handleSkipAction(2);
        action.handlePlayerTurnAction(2);
    }
}