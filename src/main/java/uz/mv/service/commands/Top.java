package uz.mv.service.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.Book;
import uz.mv.mappers.BookMapper;
import uz.mv.service.search.SearchService;
import uz.mv.util.InlineButtonUtil;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Top {
    private static final Top instance = new Top();
    private static final TBot bot = TBot.getInstance();
    public static Top getInstance() {
        return instance;
    }


    public void top(Message message) {
        List<Book> books = BookMapper.getInstance().getTopBooks(1);
        String text = SearchService.getInstance().getTextOfBooks(books);
        if (books.isEmpty())
            text = LangConfig.get(message.getChatId().toString(), "no.books") + " \uD83E\uDD37\uD83C\uDFFB\u200D♂️";//"*No books found*";
        SendMessage msg = new SendMessage(message.getChatId().toString(), text);
        msg.setParseMode("Markdown");
        msg.setReplyMarkup(InlineButtonUtil.getInstance().getTopBooksMarkup(books,1));
        bot.send(msg);
    }
}
