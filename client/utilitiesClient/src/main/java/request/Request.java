package request;

import java.util.List;

public class Request {
    private String sessionToken;
    private String method;
    private List<String> parameters;

    public Request(String sessionToken, String method, List<String> parameters) {
        this.sessionToken = sessionToken;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public String toString() {

        String result = (parameters == null) ? sessionToken + ";" + method :
                sessionToken + ";" + String.join(";", parameters) + ";" + method;

        return result;
    }
}
