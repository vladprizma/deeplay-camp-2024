package commands;

import client.Client;
import request.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SkipCommand implements Command {
    private final Client client;
    private final Request request;

    public SkipCommand(Client client, String sessionToken, int id) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(id));
        this.request = new Request(sessionToken, "resume", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
