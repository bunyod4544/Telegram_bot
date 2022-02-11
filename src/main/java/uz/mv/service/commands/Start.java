package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.enums.langSettings.Language;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.UserRepository;
import uz.mv.util.ReplyButtonUtil;

import java.util.Objects;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class Start {
    private static TBot tBot = TBot.getInstance();
    public void start(Message message){
        createUser(message);
        TBot.getInstance().sessionUser = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(LangConfig.get(message.getChatId().toString(),"welcome")+" \uD83D\uDE0A");
        sendMessage.setParseMode("Markdown");
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(message.getChatId().toString()));
        tBot.send(sendMessage);
    }

    private void createUser(Message message) {
        User user = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        if (Objects.isNull(user)){
            user = User.builder().build();
            user.setChatId(message.getChatId().toString());
            user.setFullName(message.getFrom().getFirstName() + " " +
                    (Objects.nonNull(message.getFrom().getLastName()) ?
                            message.getFrom().getLastName() : ""));
            user.setLanguage(Language.getByCode(message.getFrom().getLanguageCode()));
            user.setUsername(message.getFrom().getUserName());
            user.setState(State.UNAUTHORIZED);
            UserRepository.getInstance().saveUser(user);
        }
    }


    private static final Start instance = new Start();

    public static Start getInstance() {
        return instance;
    }
}
