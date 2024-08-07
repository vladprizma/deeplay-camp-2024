package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;

public class BotStartCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public BotStartCommandRequest(Client client) {
        this.client = client;
        this.requestResponse = new RequestResponse("game-start --bot", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
