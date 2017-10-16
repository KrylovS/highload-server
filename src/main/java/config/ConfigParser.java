package config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sergey on 10.10.17.
 */
public class ConfigParser {
    private final static String PORT = "listen";
    private final static String CPU = "cpu_limit";
    private final static String ROOT = "document_root";

    public static ServerConfig parseFile(String path) throws IOException {
        final File file = new File(path);
        if(file.exists()) {
            final List<String> configList =  Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            return parseConfig(configList);

        } else {
            return new ServerConfig(null, null, null, String.format("file %s does not exist", path));
        }
    }

    public static ServerConfig parseConfig(List<String> config) {
        final Map<String, String> paramMap = new HashMap<String, String>();

        for (String line : config) {
            final String[] buf = line.split(" ");
            if (buf.length >= 2) {
                paramMap.put(buf[0], buf[1]);
            }
        }

        ServerConfig serverConfig = new ServerConfig();

        if (paramMap.containsKey(PORT)) {
            Integer port = parseInt(paramMap.get(PORT));
            if (port == null) {
                return new ServerConfig(null, null, null, String.format("Wrong %s value!", PORT));
            }
            serverConfig.setPort(port);
        } else {
            return new ServerConfig(null, null, null, String.format("There is no %s value!", PORT));
        }

        if (paramMap.containsKey(CPU)) {
            Integer cpu = parseInt(paramMap.get(CPU));
            if (cpu == null) {
                return new ServerConfig(null, null, null, String.format("Wrong %s value!", CPU));
            }
            serverConfig.setCpuLimit(cpu);
        } else {
            return new ServerConfig(null, null, null, String.format("There is no %s value!", CPU));
        }

        if (paramMap.containsKey(ROOT)) {
            serverConfig.setRoot(paramMap.get(ROOT));
        } else {
            return new ServerConfig(null, null, null, String.format("There is no %s value!", ROOT));
        }
        return serverConfig;
    }


    private static Integer parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
