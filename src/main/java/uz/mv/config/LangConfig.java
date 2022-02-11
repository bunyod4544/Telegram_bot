package uz.mv.config;

import uz.mv.repository.UserRepository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Amonov Bunyod, ср 29.12.2021 15:48 .
 */
public class LangConfig {
    public static Properties uz;
    public static Properties ru;
    public static Properties en;
    public static String pathPre = "src/main/resources/i18n/";
    private static final UserRepository userRepository = UserRepository.getInstance();

    static {
        load();
    }

    private static void load() {

        try (
                FileReader uzFileReader = new FileReader(pathPre + "uz.properties");
                FileReader ruFileReader = new FileReader(pathPre + "ru.properties");
                FileReader enFileReader = new FileReader(pathPre + "en.properties");
        ) {
            uz = new Properties();
            ru = new Properties();
            en = new Properties();

            uz.load(uzFileReader);
            ru.load(ruFileReader);
            en.load(enFileReader);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String get(String chatId, String key) {
        String language = userRepository.findLanguage(chatId);
        if (language.equalsIgnoreCase("en")) {
            return en.getProperty(key);
        } else if (language.equalsIgnoreCase("ru")) {
            return ru.getProperty(key);
        } else return uz.getProperty(key);
    }
}
