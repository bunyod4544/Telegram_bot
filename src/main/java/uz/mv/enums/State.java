package uz.mv.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Amonov Bunyod, пт 24.12.2021 17:06 .
 */
@Getter
@AllArgsConstructor
public enum State {
    UNAUTHORIZED("UNAUTHORIZED"),
    NAME ("NAME"),
    PHONE_NUMBER("PHONE_NUMBER"),
    REGISTERED("REGISTERED"),
    ADD_BOOK("ADD_BOOK"),
    ADD_KEYWORD("ADD_KEYWORD"),
    ADD_AUTHOR("ADD_AUTHOR"),
    SEARCH("SEARCH"),
    NONE("NONE");

    private final String name;

    public static State getState(String stateName) {
        for (State value : State.values()) {
            if (value.name.equals(stateName)) return value;
        }
        return null;
    }
}
