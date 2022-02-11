package uz.mv.entity;

import lombok.*;
import uz.mv.enums.State;
import uz.mv.enums.langSettings.Language;

/**
 * @author Amonov Bunyod, пт 24.12.2021 17:03 .
 */
@Getter
@Setter
@Builder
public class User {
    private String chatId;
    private String name; //name -> o'zi kiritadi
    private String fullName; //fullName -> O'zimiz get qilib olamiz
    private String username;
    private String phoneNumber;
    private Language language;
    private State state;
    private String sessionBookId;
    private State searchState;

}
