package commands;

import client.Client;
import request.Request;

import java.io.IOException;

public class StartCommand implements Command {
    private final Client client;
    private final Request request;

    public StartCommand(Client client, String sessionToken) {
        this.client = client;
        this.request = new Request(sessionToken, "start", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
