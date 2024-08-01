package client;

import request.RequestResponse;
import java.io.IOException;

public interface Client {
    void sendRequest(RequestResponse requestResponse) throws IOException;
}
