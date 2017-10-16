package config;

/**
 * Created by sergey on 10.10.17.
 */
public class ServerConfig {
    private Integer port;
    private Integer cpuLimit;
    private String root;
    private String errors;

    public ServerConfig(Integer port, Integer cpuLimit, String root, String errors) {
        this.port = port;
        this.cpuLimit = cpuLimit;
        this.root = root;
        this.errors = errors;
    }

    public ServerConfig() {
        this.port = null;
        this.cpuLimit = null;
        this.root = null;
        this.errors = null;
    }

    public int getCpuLimit() {
        return cpuLimit;
    }

    public int getPort() {
        return port;
    }

    public String getRoot() {
        return root;
    }

    public String getErrors() {
        return errors;
    }

    public void setCpuLimit(int cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
