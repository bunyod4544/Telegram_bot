package uz.mv.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PConfig {
    private static Properties p;

    static {
        try(FileReader fileReader = new FileReader("src/main/resources/app.properties")){
            p = new Properties();
            p.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return p.getProperty(key);
    }
}
