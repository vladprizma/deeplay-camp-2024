package action;

import client.Client;
import client.ClientManager;
import commands.*;

import java.io.IOException;

public class Action {
    public void handleMoveAction(int id, int x, int y) {
        try {
            Client client = ClientManager.getClient();
            Command moveCommand = new MoveCommand(client, "sessionToken123", id, x, y);
            moveCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleStartAction(int id) {
        try {
            Client client = ClientManager.getClient();
            Command startCommand = new StartCommand(client, "sessionToken123", id);
            startCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePauseAction(int id) {
        try {
            Client client = ClientManager.getClient();
            Command pauseCommand = new PauseCommand(client, "sessionToken123", id);
            pauseCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResumeAction(int id) {
        try {
            Client client = ClientManager.getClient();
            Command resumeCommand = new ResumeCommand(client, "sessionToken123", id);
            resumeCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSkipAction(int id) {
        try {
            Client client = ClientManager.getClient();
            Command skipCommand = new SkipCommand(client, "sessionToken123", id);
            skipCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePlayerTurnAction(int id) {
        try {
            Client client = ClientManager.getClient();
            Command playerTurnCommand = new PlayerTurnCommand(client, "sessionToken123", id);
            playerTurnCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}