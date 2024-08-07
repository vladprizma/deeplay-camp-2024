package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MoveCommandRequest implements CommandRequest {
    private final Client client;
    private final RequestResponse requestResponse;

    public MoveCommandRequest(Client client, String move) {
        this.client = client;
        List<String> parameters = Arrays.asList(move);
        this.requestResponse = new RequestResponse("game-move", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(requestResponse);
    }
}
