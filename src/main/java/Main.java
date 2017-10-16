import config.ConfigParser;
import config.ServerConfig;
import server.Server;

import java.io.IOException;

/**
 * Created by sergey on 6.10.17.
 */
public class Main {
    public static final String CONFIG = "/etc/httpd.conf";

    public static void main(String[] args) throws IOException, InterruptedException {

        final ServerConfig  config = ConfigParser.parseFile(CONFIG);
        if (config.getErrors() != null) {
            throw new RuntimeException(config.getErrors());
        }

        final Server server = new Server(config);
        System.out.println("Server started");
        server.start();

        //System.out.println(config.getPort());
        //System.out.println(config.getRoot());
        //System.out.println(config.getCpuLimit());



    }
}
