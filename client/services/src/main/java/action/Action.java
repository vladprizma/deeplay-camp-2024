package action;

import client.Client;
import client.ClientManager;
import commands.Command;
import commands.MoveCommand;

import java.io.IOException;

public class Action {
    public void handleMoveAction(int x, int y, int z) {
        try {
            Client client = ClientManager.getClient();
            Command moveCommand = new MoveCommand(client, "sessionToken123", x, y, z);
            moveCommand.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
