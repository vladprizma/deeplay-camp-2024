package commands;

import client.Client;
import request.Request;

import java.io.IOException;

public class PauseCommand implements Command {
    private final Client client;
    private final Request request;

    public PauseCommand(Client client, String sessionToken) {
        this.client = client;
        this.request = new Request(sessionToken, "pause", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
