package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;

public class PauseCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public PauseCommandRequest(Client client) {
        this.client = client;
        this.requestResponse = new RequestResponse("pause", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
