package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public RegisterCommandRequest(Client client, String loginAndPassword) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(loginAndPassword));
        this.requestResponse = new RequestResponse( "register", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
