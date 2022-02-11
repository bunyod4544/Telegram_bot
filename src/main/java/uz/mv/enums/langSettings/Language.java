package uz.mv.enums.langSettings;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Amonov Bunyod, ср 29.12.2021 15:57 .
 */
@Getter
@AllArgsConstructor
public enum Language {
    UZ("uz", "Uzbek", "src/main/resources/i18n/uz.properties"),
    RU("ru", "Russian", "src/main/resources/i18n/ru.properties"),
    EN("en", "English", "src/main/resources/i18n/en.properties");

    private final String code;
    private final String name;
    private final String path;

    public static Language getByCode(String lang) {
        for (Language language : values()) {
            if (language.getCode().equalsIgnoreCase(lang)) {
                return language;
            }
        }
        return null;
    }

}