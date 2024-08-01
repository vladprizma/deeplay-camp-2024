package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;

public class ResumeCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public ResumeCommandRequest(Client client) {
        this.client = client;
        this.requestResponse = new RequestResponse("resume", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
