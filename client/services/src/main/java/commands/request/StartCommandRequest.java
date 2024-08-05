package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;

public class StartCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public StartCommandRequest(Client client) {
        this.client = client;
        this.requestResponse = new RequestResponse("game-start", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
