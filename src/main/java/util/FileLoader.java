package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileLoader {
    public static String getFilePath() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.props"));
            return properties.getProperty("DATA_FILE_PATH");
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        return null;
    }
}
