package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.util.InlineButtonUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Developers {
    private static TBot tBot = TBot.getInstance();
    public void developer(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(LangConfig.get(message.getChatId().toString(),"max.dev") +"\uD83D\uDCBB ");
        sendMessage.setParseMode("Markdown");
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(InlineButtonUtil.developer());
//            sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collect(
//                    InlineButtonUtil.row(
//                            InlineButtonUtil.button("Bunyod\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB", "dev").setUrl("https://t.me//dostonbokhodirov")),
//                    InlineButtonUtil.row(InlineButtonUtil.button("Jonibek\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB", "dev2")),
//                    InlineButtonUtil.row(InlineButtonUtil.button("Umid\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB","dev1")),
//                    InlineButtonUtil.row(InlineButtonUtil.button("Javohir\uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB", "dev3")))));
        tBot.send(sendMessage);
    }

    private static final Developers developers = new Developers();

    public static Developers getInstance() {
        return developers;
    }
}
