package commands;

import client.Client;
import request.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PlayerTurnCommand implements Command {
    private final Client client;
    private final Request request;

    public PlayerTurnCommand(Client client, String sessionToken) {
        this.client = client;
        this.request = new Request(sessionToken, "turn", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
