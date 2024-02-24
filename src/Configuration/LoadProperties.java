package Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {
    private static final String FILE_PATH = "src/Configuration/Settings.properties";
    private static Properties properties;

    private LoadProperties() {}

    public static Properties getInstance() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fileIS = new FileInputStream(FILE_PATH)) {
                properties.load(fileIS);
            } catch (IOException e) {
                System.out.println("Error when loading properties");
                e.printStackTrace();
            }
        }
        return properties;
    }
}
