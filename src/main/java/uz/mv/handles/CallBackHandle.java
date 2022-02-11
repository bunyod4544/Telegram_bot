package uz.mv.handles;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.Book;
import uz.mv.mappers.BookMapper;
import uz.mv.repository.BookRepository;
import uz.mv.repository.GeneralRepository;
import uz.mv.service.search.SearchService;
import uz.mv.util.InlineButtonUtil;
import uz.mv.util.ReplyButtonUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author Amonov Bunyod, пт 24.12.2021 14:31 .
 */

public class CallBackHandle {
    private static final TBot bot = TBot.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static SendMessage sendMessage = new SendMessage();

    public static void handleCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();
        ResultSet resultSet = GeneralRepository.getInstance().getNextPrevious(data);
        if (data.equals("uz") || data.equals("en") || data.equals("ru")) {
            bookRepository.save(data, chatId);
            sendMessage.setText(LangConfig.get(chatId, "lang.success") + "\uD83D\uDC4D");
            sendMessage.setParseMode("Markdown");
            sendMessage.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(chatId));
            sendMessage.setChatId(chatId);
            bot.send(sendMessage);
            DeleteMessage deleteMessage = new DeleteMessage(chatId, callbackQuery.getMessage().getMessageId());
            bot.send(deleteMessage);
        } else
            try {
                if (Objects.nonNull(resultSet) && resultSet.next()) {
                    nextPrevious(callbackQuery, resultSet);
                } else if (data.startsWith("t")) {
                    int page = Integer.parseInt(data.substring(1));
                    topNextPrevious(callbackQuery, page);
                } else {
                    Book book = BookMapper.getInstance().getBookById(Integer.parseInt(data));
                    String caption = "✍️*" + book.getAuthor() + "\n\uD83D\uDCBE " +
                            SearchService.getInstance().convertSizeToMB(book.getSize()) +" MB"+
                            "\n\uD83D\uDCE5 " + book.getDownloadCount() +
                            "\n\n\uD83D\uDC49 *||@mv\\_library\\_bot||";
                    InputFile file = new InputFile(book.getFileId());
                    SendDocument doc = new SendDocument(callbackQuery.getMessage().getChatId().toString(), file);
                    doc.setCaption(caption);
                    doc.setParseMode("MarkdownV2");
                    bot.send(doc);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


    }

    private static void topNextPrevious(CallbackQuery callbackQuery, int page) {
        List<Book> books = BookMapper.getInstance().getTopBooks(page);
        String messageText = SearchService.getInstance().getTextOfBooks(books);
        if (books.isEmpty())
            messageText = LangConfig.get(callbackQuery.getMessage().getChatId().toString(), "no.books") + " \uD83E\uDD37\uD83C\uDFFB\u200D♂️";//"*No books found*";
        InlineKeyboardMarkup markup =
                InlineButtonUtil.getInstance().getTopBooksMarkup(books, page);

        EditMessageText emsg = new EditMessageText(messageText);
        emsg.setChatId(callbackQuery.getMessage().getChatId().toString());
        emsg.setMessageId(callbackQuery.getMessage().getMessageId());
        emsg.setParseMode("Markdown");

        EditMessageReplyMarkup emkp = new EditMessageReplyMarkup();
        emkp.setChatId(callbackQuery.getMessage().getChatId().toString());
        emkp.setMessageId(callbackQuery.getMessage().getMessageId());
        emkp.setReplyMarkup(markup);

        bot.send(emsg);
        bot.send(emkp);
    }

    private static void nextPrevious(CallbackQuery callbackQuery, ResultSet resultSet) throws SQLException {
        String text = resultSet.getString("search_text");
        int page = resultSet.getInt("page");
        List<Book> books = BookMapper.getInstance().getBooksByKeyword(text, page);
        String messageText = SearchService.getInstance().getTextOfBooks(books);
        if (books.isEmpty())
            messageText = LangConfig.get(callbackQuery.getMessage().getChatId().toString(), "no.books") + " \uD83E\uDD37\uD83C\uDFFB\u200D♂️";//"*No books found*";
        InlineKeyboardMarkup markup =
                InlineButtonUtil.getInstance().getInlineKeyboardMarkupForBooks(books, page, text);

        EditMessageText emsg = new EditMessageText(messageText);
        emsg.setChatId(callbackQuery.getMessage().getChatId().toString());
        emsg.setMessageId(callbackQuery.getMessage().getMessageId());
        emsg.setParseMode("Markdown");

        EditMessageReplyMarkup emkp = new EditMessageReplyMarkup();
        emkp.setChatId(callbackQuery.getMessage().getChatId().toString());
        emkp.setMessageId(callbackQuery.getMessage().getMessageId());
        emkp.setReplyMarkup(markup);

        bot.send(emsg);
        bot.send(emkp);
    }
}
