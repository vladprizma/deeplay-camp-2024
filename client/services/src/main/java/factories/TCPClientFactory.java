package factories;

import client.Client;
import client.TCPClient;
import io.deeplay.camp.config.LoadServerProperties;

import java.io.IOException;
import java.util.Properties;

public class TCPClientFactory {
    public static Client createClient() throws IOException {
        Properties config = LoadServerProperties.loadConfig();
        String serverIp = config.getProperty("server.ip");
        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        return new TCPClient(serverIp, serverPort);
    }
}
