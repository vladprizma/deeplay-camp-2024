package commands;

import client.Client;
import request.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StartCommand implements Command {
    private final Client client;
    private final Request request;

    public StartCommand(Client client, String sessionToken, int id) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(id));
        this.request = new Request(sessionToken, "start", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
