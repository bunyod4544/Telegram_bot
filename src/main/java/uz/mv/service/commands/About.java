package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.util.InlineButtonUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class About {
    private static TBot tBot = TBot.getInstance();
    public void about(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(String.format(LangConfig.get(message.getChatId().toString(),"about"),"\uD83D\uDCD6","\uD83D\uDC7D","\uD83D\uDCDA","\uD83D\uDD0E","\uD83D\uDCBB"));
        sendMessage.setParseMode("Markdown");
        sendMessage.setChatId(message.getChatId().toString());
        tBot.send(sendMessage);
    }

    private static final About instance = new About();

    public static About getInstance() {
        return instance;
    }
}
