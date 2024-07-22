package client;

import factories.TCPClientFactory;

import java.io.IOException;

public class ClientManager {
    private static Client clientInstance;

    public static synchronized Client getClient() throws IOException {
        if (clientInstance == null) {
            clientInstance = TCPClientFactory.createClient();
        }
        return clientInstance;
    }
}
