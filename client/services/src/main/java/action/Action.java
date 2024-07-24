package action;

import client.Client;
import client.ClientManager;
import commands.*;

import java.io.IOException;

public class Action {
    private Client client;

    public Action() throws IOException {
        client = ClientManager.getClient();
    }

    public void handleMoveAction(int x, int y) {
        try {
            Command moveCommand = new MoveCommand(client, "sessionToken123", x, y);
            moveCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStartAction() {
        try {
            Command startCommand = new StartCommand(client, "sessionToken123");
            startCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePauseAction() {
        try {
            Command pauseCommand = new PauseCommand(client, "sessionToken123");
            pauseCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResumeAction() {
        try {
            Command resumeCommand = new ResumeCommand(client, "sessionToken123");
            resumeCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSkipAction() {
        try {
            Command skipCommand = new SkipCommand(client, "sessionToken123");
            skipCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePlayerTurnAction() {
        try {
            Command playerTurnCommand = new PlayerTurnCommand(client, "sessionToken123");
            playerTurnCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}