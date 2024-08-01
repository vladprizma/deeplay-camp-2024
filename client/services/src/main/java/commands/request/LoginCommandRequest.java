package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LoginCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public LoginCommandRequest(Client client, String loginAndPassword) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(loginAndPassword));
        this.requestResponse = new RequestResponse( "login", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
