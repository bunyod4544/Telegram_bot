package uz.mv.util;

import jdk.jshell.JShell;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.mv.config.LangConfig;

import java.util.List;

import static uz.mv.config.Emojis.PHONE;

/**
 * @author Amonov Bunyod, пт 24.12.2021 18:45 .
 */
@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class ReplyButtonUtil {

    public ReplyKeyboardMarkup userMenu(String chatId) {
        ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(LangConfig.get(chatId,"b.search")));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(LangConfig.get(chatId,"b.add")));
        row2.add(new KeyboardButton(LangConfig.get(chatId,"b.my.books")));

        board.setKeyboard(List.of(row1, row2));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }
    public ReplyKeyboardMarkup back(String chatId) {
        ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(LangConfig.get(chatId,"back")));
        board.setKeyboard(List.of(row1));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public ReplyKeyboardMarkup shareContact(String chatId){
        ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();
        KeyboardButton shareContact = new KeyboardButton(PHONE + LangConfig.get(chatId,"b.share.number"));
        shareContact.setRequestContact(true);
        KeyboardRow row = new KeyboardRow();
        row.add(shareContact);
        board.setKeyboard(List.of(row));
        board.setResizeKeyboard(true);
        return board;
    }


    private static ReplyButtonUtil instance = new ReplyButtonUtil();

    public static ReplyButtonUtil getInstance() {
        return instance;
    }

}
