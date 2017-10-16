package http;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static http.HttpAttr.*;

/**
 * Created by sergey on 12.10.17.
 */
public class Response {
    private String path;
    private String status;
    private String responseAnswer;
    private Request request;
    private FileWorker fileWorker = null;
    private static final String HTTP_VERSION = "HTTP/1.1";
    private Long contentLength = null;
    private String contentType = null;


    public Response(Request request) throws IOException {
        this.request = request;
        if (isAllowedMethods(request.getMethod())) {
            //String pathDec = request.getPath();
            //pathDec = URLDecoder.decode(pathDec, "UTF-8");
            path = getValidPath(request.getPath());
            if (path != null) {
                fileWorker = new FileWorker(path);
            }
            if (fileWorker != null && !fileWorker.isForbidden() && fileWorker.isFileExists()) {
                contentLength = fileWorker.fileSize();
                contentType = fileWorker.isDir() ? HttpAttr.getContentType("html") : getContentType();
            }
        }
        setStatus();
        writeResponse();
    }

    private void writeHeaders() throws IOException {
        String buf = HTTP_VERSION + " " + status + "\r\n";
        responseAnswer = buf;

        buf = "";
        if (contentLength != null) {
            buf += "Content-Length:" + " " + contentLength + "\r\n";
        }
        if (contentType != null) {
            buf += "Content-Type:" + " " + contentType + "\r\n";
        }
        final String date = getServerTime();
        buf += "Date:" + " " + date + "\n";
        buf += "Server:" + " " + "NAMENAME" + "\n";
        buf += "Connection:" + " " + "CONNECT" + "\r\n\r\n";
        responseAnswer += buf;
        //buf.append("Server:").append(' ').append(SERVER).append('\n');
        //buf.append("Connection:").append(' ').append(CONNECTION).append("\r\n\r\n");
        //out.write(buf.toString().getBytes());
    }

    public void writeResponse() throws IOException {
        writeHeaders();
    }
    /*public void writeResponse() throws IOException {
        writeHeaders();
        if (fileWorker != null && !fileWorker.isForbidden() && request.getMethod().equals(METHOD_GET) && isTextFile() && fileWorker.isFileExists()) {
            writeFile();
        }
    }*/

    private void writeFile() throws IOException{
        try {
            final List<String> fileLines = Files.readAllLines(Paths.get(fileWorker.getFullPath()));
            for (String line : fileLines) {
                responseAnswer += line + "\n";
            }
        } catch (FileNotFoundException ignored) {}
    }

    private static String getServerTime() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEEEEEEEEE, dd MMMMMMMMMM yyyy HH:mm:ss z",
                Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }


    public void setStatus() {
        status = STATUS_OK;
        if(!isAllowedMethods(request.getMethod())) {
            status = STATUS_MNA;
        } else if (fileWorker == null ||  (!fileWorker.isForbidden() &&!fileWorker.isDir() && !fileWorker.isFileExists())) {
            status = STATUS_NOTFOUND;
        } else if ((fileWorker != null && fileWorker.isForbidden()) || !fileWorker.canRead()) {//} else if (fileWorker != null && fileWorker.isFileExists() && (fileWorker.isForbidden()) || !fileWorker.canRead()) {
            status = STATUS_FORBIDDEN;
        }
    }

    public String getContentType() {
        final String[] temp = path.split("\\.");
        if(temp.length < 1) {
            return null;
        } else {
            return HttpAttr.getContentType(temp[temp.length - 1]);
        }
    }

    public Boolean isTextFile() {
        return (contentType == null || contentType.equals(HttpAttr.getContentType("html")) ||
                contentType.equals(HttpAttr.getContentType("css")) ||
                contentType.equals(HttpAttr.getContentType("js")));
    }

    public String getResponseAnswer() {
        return responseAnswer;
    }

    public String getFullPath() {
        return  fileWorker.getFullPath();
    }

    private String getValidPath(String path) throws UnsupportedEncodingException {
        path = URLDecoder.decode(path, "UTF-8");
        if (path.contains("?")) {
            return path.substring(0, path.indexOf("?"));
        }
        return path;
    }

    public boolean needWriteFile() {
        return status.equals(STATUS_OK) && request.getMethod().equals(METHOD_GET);
    }
}
