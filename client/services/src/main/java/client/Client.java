package client;

import request.Request;
import java.io.IOException;

public interface Client {
    void sendRequest(Request request) throws IOException;
}
