package uz.mv.handles;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.UserRepository;
import uz.mv.service.book.BookService;
import uz.mv.service.commands.*;
import uz.mv.service.search.SearchService;
import uz.mv.util.ReplyButtonUtil;

import java.util.Objects;

/**
 * @author Amonov Bunyod, пт 24.12.2021 14:31 .
 */

public class MsgHandle {
    private static TBot bot = TBot.getInstance();

    public static void handleMessage(Message message) {
        User user = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        String chatId = message.getChatId().toString();

        String language = UserRepository.getInstance().findLanguage(chatId);
        String text = message.getText();
        if ("/start".equals(text)) {
            Start.getInstance().start(message);
            return;
        }
        if ((user.getState().equals(State.ADD_BOOK) || (user.getState().equals(State.ADD_KEYWORD)))
                && Objects.nonNull(message.getText())
                && message.getText().equals(LangConfig.get(chatId, "back"))) {
            user.setState(State.REGISTERED);
            UserRepository.getInstance().updateUser(user);
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "backing") +"⤵️");
            sendMessage.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(chatId));
            bot.send(sendMessage);
        } else if (user.getSearchState().equals(State.SEARCH) &&(message.getText().equals(LangConfig.get(chatId, "back")))){
            user.setSearchState(State.NONE);
            UserRepository.getInstance().updateUser(user);
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "backing")+"⤵️");
            sendMessage.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(chatId));
            bot.send(sendMessage);
        } else {
            if (user.getState().getName().startsWith("ADD")) BookService.getInstance().addBook(message);
            else if (user.getSearchState().getName().startsWith("SEARCH")) SearchService.getInstance().search(message);
            else if (State.PHONE_NUMBER.equals(user.getState()) || State.NAME.equals(user.getState()))
                Register.getInstance().register(message);
        }
        if ("/register".equals(text)) Register.getInstance().register(message);
        else if ("/language".equals(text)) Language.getInstance().language(message);
        else if ("/about".equals(text)) About.getInstance().about(message);
        else if ("/developers".equals(text)) Developers.getInstance().developer(message);
        else if ("/top".equals(text)) Top.getInstance().top(message);
        else if (LangConfig.get(chatId, "b.search").equals(text)) SearchService.getInstance().search(message);
        else if (LangConfig.get(chatId, "b.add").equals(text)) BookService.getInstance().addBook(message);
        else if (LangConfig.get(chatId, "b.my.books").equals(text)) BookService.getInstance().myBooks(message);


    }

}
