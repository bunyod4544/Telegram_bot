package uz.mv.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.mv.config.Emojis;
import uz.mv.config.LangConfig;
import uz.mv.entity.Book;
import uz.mv.repository.GeneralRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Amonov Bunyod, пт 24.12.2021 18:41 .
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InlineButtonUtil {

    public static InlineKeyboardMarkup developer() {
        InlineKeyboardMarkup devKeyboard = new InlineKeyboardMarkup();

        InlineKeyboardButton first = new InlineKeyboardButton("Bunyod");
        first.setUrl("t.me//omonov_4");
        first.setCallbackData("first");
        InlineKeyboardButton second = new InlineKeyboardButton("Jonibek");
        second.setUrl("t.me//thejwa");
        second.setCallbackData("second");
        InlineKeyboardButton third = new InlineKeyboardButton("Umid");
        third.setUrl("t.me//umidmaster98");
        third.setCallbackData("third");
        InlineKeyboardButton fourth = new InlineKeyboardButton("Javohir");
        fourth.setCallbackData("fourth");
        fourth.setUrl("https://t.me//javohir_8666");

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        List<InlineKeyboardButton> buttons3 = new ArrayList<>();
        buttons.add(first);
        buttons1.add(second);
        buttons2.add(third);
        buttons3.add(fourth);

        devKeyboard.setKeyboard(List.of(buttons, buttons1, buttons2, buttons3));
        return devKeyboard;
    }

    public static ReplyKeyboard gender() {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        InlineKeyboardButton male = new InlineKeyboardButton(" Male");
        male.setCallbackData("male");
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(male);

        InlineKeyboardButton female = new InlineKeyboardButton(" Female");
        female.setCallbackData("female");
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(female);

        board.setKeyboard(List.of(buttons, buttons1));
        return board;
    }


    public static InlineKeyboardMarkup language(Message message) {
        InlineKeyboardMarkup kboard = new InlineKeyboardMarkup();


        InlineKeyboardButton uz = new InlineKeyboardButton("O'zbekcha \uD83C\uDDFA\uD83C\uDDFF");
        uz.setCallbackData("uz");

        InlineKeyboardButton ru = new InlineKeyboardButton("Русский \uD83C\uDDF7\uD83C\uDDFA");
        ru.setCallbackData("ru");

        InlineKeyboardButton en = new InlineKeyboardButton("English \uD83C\uDDEC\uD83C\uDDE7");
        en.setCallbackData("en");

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(uz);
        buttons.add(ru);
        buttons.add(en);

        kboard.setKeyboard(List.of(buttons));
        return kboard;
    }

    //    public static InlineKeyboardMarkup gender() {
//        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
//
//        InlineKeyboardButton male = new InlineKeyboardButton("Male");
//        male.setCallbackData("male");
//
//        InlineKeyboardButton female = new InlineKeyboardButton("Female");
//        male.setCallbackData("female");
//
//        List<InlineKeyboardButton> buttons = new ArrayList<>();
//        buttons.add(male);
//        buttons.add(female);
//
//        board.setKeyboard(List.of(buttons));
//        return board;
//    }
    public InlineKeyboardMarkup getInlineKeyboardMarkupForBooks(List<Book> books, int page, String searchingText) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        int index = 1;
        for (Book book : books) {
            row.add(button(Emojis.numbers[index++], book.getId() + ""));
        }

        if (page > 1) {
            String code = BaseUtil.genCode();
            row2.add(button(Emojis.previous, code));
            GeneralRepository.getInstance().saveNextPrevious(code, page - 1, searchingText);
        }
        if (books.size() == 5) {
            String code = BaseUtil.genCode();
            row2.add(button(Emojis.next, code));
            GeneralRepository.getInstance().saveNextPrevious(code, page + 1, searchingText);
        }
        return keyboard(collect(row, row2));

    }

    public InlineKeyboardMarkup getTopBooksMarkup(List<Book> books, int page) {
        List<InlineKeyboardButton> row = new ArrayList();
        List<InlineKeyboardButton> row2 = new ArrayList();
        int index = 1;
        for (Book book : books) {
            row.add(button(Emojis.numbers[index++], book.getId() + ""));
        }

        String data;
        if (page > 1) {
            data = "t" + (page - 1);
            row2.add(button(Emojis.previous, data));
        }

        if (books.size() == 5) {
            data = "t" + (page + 1);
            row2.add(button(Emojis.next, data));
        }

        return keyboard(collect(row, row2));
    }


    public static InlineKeyboardButton button(String text, String callBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return button;
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }

    public static List<List<InlineKeyboardButton>> collect(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> collect = new ArrayList<>();
        collect.addAll(Arrays.asList(rows));
        return collect;
    }

    public static InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> collect) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(collect);
        return inlineKeyboardMarkup;
    }



    private static final InlineButtonUtil instance = new InlineButtonUtil();

    public static InlineButtonUtil getInstance() {
        return instance;
    }

}
