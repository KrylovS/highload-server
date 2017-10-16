package http;

import static http.HttpAttr.isAllowedMethods;

/**
 * Created by sergey on 12.10.17.
 */
public class Request {
    private String path = null;
    private String method = null;


    public Request(String request) {
        if(request != null && !request.equals("") && request.contains("HTTP")) {
            final String[] lines = request.split("\\n");
            final String[] words = lines[0].split(" ");
            path = words[1];//.replace("../", "");
            method = isAllowedMethods(words[0]) ? words[0] : null;
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
