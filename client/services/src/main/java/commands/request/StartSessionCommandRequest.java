package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StartSessionCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public StartSessionCommandRequest(Client client, String updateToken) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(updateToken));
        this.requestResponse = new RequestResponse("session-start", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
