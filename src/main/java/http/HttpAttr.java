package http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sergey on 11.10.17.
 */
public class HttpAttr {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";

    public static boolean isAllowedMethods(String method) {
        return (method != null) && (method.equals(METHOD_GET) || method.equals(METHOD_HEAD));
    }

    private static final Map<String, String> contentType = new HashMap<>();

    static {
        contentType.put("html","text/html");
        contentType.put("css","text/css");
        contentType.put("js","application/javascript");
        contentType.put("jpeg","image/jpeg");
        contentType.put("jpg","image/jpeg");
        contentType.put("png","image/png");
        contentType.put("gif","image/gif");
        contentType.put("swf","application/x-shockwave-flash");
        contentType.put("plain","text/plain");
    }

    public static final String STATUS_OK = "200 OK";
    public static final String STATUS_FORBIDDEN = "403 Forbidden";
    public static final String STATUS_NOTFOUND = "404 Not Found";
    public static final String STATUS_MNA = "405 Method Not Allowed";

    public static String getContentType(String type) {
        return contentType.containsKey(type) ? contentType.get(type) : null;
    }

}
