package uz.mv.service.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import uz.mv.TBot;
import uz.mv.config.Emojis;
import uz.mv.config.LangConfig;
import uz.mv.entity.Book;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.mappers.BookMapper;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.UserRepository;
import uz.mv.util.InlineButtonUtil;
import uz.mv.util.ReplyButtonUtil;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchService {
    private static SearchService instance = new SearchService();
    private static final TBot bot = TBot.getInstance();

    public static SearchService getInstance() {
        return instance;
    }

    public void search(Message message) {
        User user = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        if (!State.SEARCH.equals(user.getSearchState())) sendSearchText(message, user);
        else showResults(message, user);
    }

    private void sendSearchText(Message message, User user) {
        SendMessage msg = new SendMessage(message.getChatId().toString(),
                LangConfig.get(message.getChatId().toString(), "search.name"));
        msg.setParseMode("Markdown");
        msg.setReplyMarkup(ReplyButtonUtil.getInstance().back(message.getChatId() + ""));
        bot.send(msg);
        user.setSearchState(State.SEARCH);
        UserRepository.getInstance().updateUser(user);
    }

    public void showResults(Message message, User user) {
        String text = message.getText();
        List<Book> books = BookMapper.getInstance().getBooksByKeyword(text,1);
        user.setSearchState(State.NONE);
        UserRepository.getInstance().updateUser(user);
        if (books.isEmpty()) {
            SendMessage msg = new SendMessage(message.getChatId().toString(),
                    LangConfig.get(message.getChatId().toString(),"no.books")+" \uD83E\uDD37\uD83C\uDFFB\u200D♂️");
            msg.setParseMode("MarkdownV2");
            msg.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(message.getChatId() +""));
            bot.send(msg);
            return;
        }
        SendMessage msg = new SendMessage();
        msg.setChatId(message.getChatId().toString());
        msg.setText(getTextOfBooks(books));
        msg.setReplyMarkup(InlineButtonUtil.getInstance().getInlineKeyboardMarkupForBooks(books,1,text));
        msg.setParseMode("Markdown");
        bot.send(msg);
        String chatId = message.getChatId() + "";
        SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "backing") +" ⬇️");
        sendMessage.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(chatId));
        bot.send(sendMessage);
    }

    public String getTextOfBooks(List<Book> books) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < books.size(); i++) {
            result.append(Emojis.numbers[i + 1]).append("\n*").append(books.get(i).getName()).append("\n")
                    .append(Emojis.disk).append(" ").append(convertSizeToMB(books.get(i).getSize())).append(" MB").append("\t\t")
                    .append(Emojis.download).append(" ").append(books.get(i).getDownloadCount()).append("*\n\n");
        }
        return result.toString();
    }

    public String convertSizeToMB(String size) {
        float sizeFloat = Float.parseFloat(size) / 1024 / 1024;
        size = String.format("%.01f",sizeFloat);
        return size;
    }
}
