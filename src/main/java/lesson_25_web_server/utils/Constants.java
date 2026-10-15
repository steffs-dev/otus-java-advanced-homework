package lesson_25_web_server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constants {
    private static final Properties props = new Properties();

    static {
        try (
                InputStream input = Constants.class.getClassLoader().
                        getResourceAsStream("lesson_25_web_server/constants.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
