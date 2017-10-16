package http;

import server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by sergey on 12.10.17.
 */
public class FileWorker {
    public static final String INDEX_REQ = "/";
    public static final String INDEX_HTML = "httptest/dir2/index.html";
    public static final String INDEX_DIR = "index.html";
    public static final String ROOT_DIR = Server.getRoot();
    private String fullPath;

    private File file;
    private boolean isDir = false;
    private boolean isForb = false;

    public FileWorker(String path) {
        if (path.contains("../")) {
            file = null;
            isForb = true;
            return;
        }
        file = new File(ROOT_DIR + path);
        if (file.isDirectory()) {
            if (!path.equals(INDEX_REQ)) {
                fullPath = ROOT_DIR + path + INDEX_DIR;
            } else {
                fullPath = ROOT_DIR + path + INDEX_HTML;
            }
            file = new File(fullPath);
            isDir = true;
        } else {
            fullPath = ROOT_DIR + path;
        }
    }

    public FileInputStream getFile() throws FileNotFoundException {
        return new FileInputStream(file.getAbsoluteFile());
    }

    public boolean isFileExists(){
        return file.exists();
    }

    public boolean isDir(){
        return isDir;
    }

    public boolean canRead(){
        return file.canRead();
    }

    public long fileSize() {
        return file.length();
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isForbidden() {
        return isForb;
    }

}