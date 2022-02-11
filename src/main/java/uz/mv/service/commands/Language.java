package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.util.InlineButtonUtil;
import uz.mv.util.ReplyButtonUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Language {

    private static TBot tBot = TBot.getInstance();

    public void language(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(LangConfig.get(message.getChatId().toString(),"language"));
        sendMessage.setParseMode("Markdown");
        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collect(
                InlineButtonUtil.row(InlineButtonUtil.button("O'zbekcha "+"\uD83C\uDDFA\uD83C\uDDFF", "uz")),
                InlineButtonUtil.row(InlineButtonUtil.button("Русский "+"\uD83C\uDDF7\uD83C\uDDFA", "ru")),
                InlineButtonUtil.row(InlineButtonUtil.button("English"+"\uD83C\uDDEC\uD83C\uDDE7", "en")))));
        tBot.send(sendMessage);

    }

    private static final Language language = new Language();

    public static Language getInstance() {
        return language;
    }
}
