package commands;

import client.Client;
import request.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MoveCommand implements Command {
    private final Client client;
    private final Request request;

    public MoveCommand(Client client, String sessionToken, int x, int y, int z) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(x), String.valueOf(y), String.valueOf(z));
        this.request = new Request(sessionToken, "move", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(request);
    }
}
