package commands.request;

import client.Client;
import request.RequestResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChatCommandRequest implements CommandRequest{
    private final Client client;
    private final RequestResponse chatMessages;

    public ChatCommandRequest(Client client, String chatMessages) {
        this.client = client;
        List<String> parameters = Arrays.asList(String.valueOf(chatMessages));
        this.chatMessages = new RequestResponse( "send-global-message", parameters);
    }

    @Override
    public void execute() throws IOException {
        client.sendRequest(chatMessages);
    }
}
