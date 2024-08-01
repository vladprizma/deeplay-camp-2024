package request;

import java.util.List;

public class RequestResponse {
    private String method;
    private List<String> parameters;

    public RequestResponse(String method, List<String> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public String toString() {

        String result = (parameters == null) ? method :
                method + " " + String.join(" ", parameters);

        return result;
    }
}
