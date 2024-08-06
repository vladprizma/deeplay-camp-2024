package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;

public class BoardCommandRequest implements CommandRequest{
    private final Client client;
    private final RequestResponse board;

    public BoardCommandRequest(Client client) {
        this.client = client;
        this.board = new RequestResponse( "get-board", null);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(board);
    }
}
