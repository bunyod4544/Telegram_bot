package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.UserRepository;
import uz.mv.util.Checks;
import uz.mv.util.ReplyButtonUtil;

import static uz.mv.config.Emojis.*;

/**
 * @author Amonov Bunyod, пт 24.12.2021 16:46 .
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Register {
    private static TBot bot = TBot.getInstance();

    public void register(Message message) {
        String chatId = message.getChatId().toString();
        User user = UserMapper.getInstance().getByChatId(chatId);
        State state = user.getState();

        if (State.UNAUTHORIZED.equals(state)) askName(message,user);
        else if (State.NAME.equals(state)) askPhoneNumber(message, user);
        else if (State.PHONE_NUMBER.equals(state)) registerUser(message, user);
        else if (State.REGISTERED.equals(state)) {
            SendMessage msg = new SendMessage(user.getChatId(),
                    String.format(LangConfig.get(chatId, "register"),"\uD83D\uDCD6"));
            msg.setParseMode("Markdown");
            bot.send(msg);
        }

    }

    private void registerUser(Message message, User user) {
        if (message.hasContact()){
            user.setPhoneNumber(message.getContact().getPhoneNumber());
            user.setState(State.REGISTERED);
            UserRepository.getInstance().updateUser(user);
            SendMessage msg = new SendMessage(user.getChatId(),
                    LangConfig.get(message.getChatId().toString(),"reg.success"));
            msg.setParseMode("Markdown");
            msg.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(message.getChatId().toString()));
            bot.send(msg);
        }else {
            SendMessage msg = new SendMessage(user.getChatId(),
                    LangConfig.get(message.getChatId().toString(),"b.share.number") + PHONE);
            bot.send(msg);
        }
    }

    private void askPhoneNumber(Message message, User user) {
        if (!Checks.checkName(message.getText())) {
            SendMessage msg = new SendMessage(user.getChatId(),
                    UNDOV + LangConfig.get(message.getChatId().toString(),"valid.name") +
                            BLUE_RHOMBUS + LangConfig.get(message.getChatId().toString(),"first.letter") +
                            BLUE_RHOMBUS + LangConfig.get(message.getChatId().toString(),"symbol"));
            msg.setParseMode("Markdown");
            bot.send(msg);
        } else {
            user.setName(message.getText());
            user.setState(State.PHONE_NUMBER);
            UserRepository.getInstance().updateUser(user);
            SendMessage msg = new SendMessage(
                    user.getChatId(),
                    LangConfig.get(message.getChatId().toString(),"b.share.number") + PHONE);
            msg.setReplyMarkup(ReplyButtonUtil.getInstance().shareContact(message.getChatId().toString()));
            bot.send(msg);
        }
    }

    private void askName(Message message, User user) {
        SendMessage msg = new SendMessage(message.getChatId().toString(),
                LangConfig.get(message.getChatId().toString(),"send.name"));
        msg.setParseMode("Markdown");
        msg.setReplyMarkup(new ReplyKeyboardRemove(true, false));
//        msg.setReplyMarkup(new ReplyKeyboardMarkup());
        bot.send(msg);
        user.setState(State.NAME);
        UserRepository.getInstance().updateUser(user);
    }


    private static Register instance = new Register();

    public static Register getInstance() {
        return instance;
    }
}
