package uz.mv.service.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.language.bm.Lang;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import uz.mv.TBot;
import uz.mv.config.LangConfig;
import uz.mv.entity.Book;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.mappers.BookMapper;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.BookRepository;
import uz.mv.repository.UserRepository;
import uz.mv.service.search.SearchService;
import uz.mv.util.ReplyButtonUtil;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookService {
    private static final BookService instance = new BookService();
    private static final TBot bot = TBot.getInstance();
    private static final String bookTypes = ".pdf .docx .pptx .djvu .epub .zip .rar .7z";

    public void addBook(Message message) {
        String chatId = message.getChatId().toString();
        User user = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        if (State.UNAUTHORIZED.equals(user.getState())) {
            SendMessage msg = new SendMessage(message.getChatId().toString(), "\uD83D\uDCD2" + LangConfig.get(chatId, "add.register"));
            msg.setParseMode("Markdown");
            bot.send(msg);
        } else {
            State state = user.getState();
            if (State.REGISTERED.equals(state)) initialMessage(message, user);
            else if (State.ADD_BOOK.equals(state)) saveDocument(message, user);
            else if (State.ADD_AUTHOR.equals(state)
                    && Objects.nonNull(message.getText())) saveAuthor(message, user);
            else if (State.ADD_KEYWORD.equals(state)
                    && Objects.nonNull(message.getText())) saveKeywords(message, user);
            else {
                SendMessage msg = new SendMessage(chatId,
                        LangConfig.get(chatId, "invalid.format"));
                msg.setParseMode("Markdown");
                bot.send(msg);
            }
        }


    }

    private void initialMessage(Message message, User user) {
        String chatId = message.getChatId().toString();
        SendMessage msg = new SendMessage(message.getChatId().toString(),
                LangConfig.get(chatId, "send.book"));
        msg.setReplyMarkup(ReplyButtonUtil.getInstance().back(chatId));
        msg.setParseMode("Markdown");
        user.setState(State.ADD_BOOK);
        UserRepository.getInstance().updateUser(user);
        bot.send(msg);

    }

    private void saveDocument(Message message, User user) {
        String chatId = message.getChatId().toString();
        if (message.hasDocument()) {
            Document document = message.getDocument();
            System.out.println("document.getMimeType() = " + document.getMimeType());
            String[] documentName = document.getFileName().split("\\.");
            if (bookTypes.contains(documentName[documentName.length-1])) {
                String nameOfBook = document.getFileName();
                String fileId = document.getFileId();
                String fileSize = document.getFileSize().toString();
                String ownerId = message.getChatId().toString();
                String type = documentName[documentName.length-1];
                Book book = Book.builder()
                        .downloadCount(0)
                        .name(nameOfBook)
                        .fileId(fileId)
                        .ownerId(ownerId)
                        .type(type)
                        .size(fileSize)
                        .build();
                BookRepository.getInstance().save(book);
//                BookRepository.getInstance().saveKeywords(fileId, nameOfBook);
                user.setState(State.ADD_AUTHOR);
                user.setSessionBookId(fileId);
                UserRepository.getInstance().updateUser(user);
                SendMessage msg = new SendMessage(message.getChatId().toString(),
                        LangConfig.get(chatId, "ask.author") + " \uD83D\uDD8C");
                msg.setParseMode("Markdown");
                bot.send(msg);
            } else {
                SendMessage msg = new SendMessage(message.getChatId().toString(),
                        LangConfig.get(chatId, "we.support") + "⁉️" + bookTypes + LangConfig.get(chatId, "doc.type"));
                bot.send(msg);
            }
        } else {
            SendMessage msg = new SendMessage(message.getChatId().toString(),
                    LangConfig.get(chatId, "corr.file") + "⁉️");
            msg.setParseMode("Markdown");
            bot.send(msg);
        }

    }

    private void saveAuthor(Message message, User user) {
        String chatId = message.getChatId().toString();
        String author = message.getText();
        Book book = BookMapper.getInstance().getBookByFileId(user.getSessionBookId());
        book.setAuthor(author);
        BookRepository.getInstance().updateBook(book);
        user.setState(State.ADD_KEYWORD);
        UserRepository.getInstance().updateUser(user);
        SendMessage msg = new SendMessage(message.getChatId().toString(),
                LangConfig.get(chatId, "send.keyword") + " \uD83D\uDDDD");
        msg.setParseMode("Markdown");
        bot.send(msg);


    }

    private void saveKeywords(Message message, User user) {
        String chatId = message.getChatId().toString();
        Book book = BookMapper.getInstance().getBookByFileId(user.getSessionBookId());
        String keyword = book.getName() + book.getOwnerId() + book.getAuthor() + message.getText();
        BookRepository.getInstance().saveKeywords(user.getSessionBookId(), keyword);
        user.setSessionBookId("NONE");
        user.setState(State.REGISTERED);
        UserRepository.getInstance().updateUser(user);
        SendMessage msg = new SendMessage(message.getChatId().toString(),
                LangConfig.get(chatId, "book.added") + "✅");
        msg.setReplyMarkup(ReplyButtonUtil.getInstance().userMenu(chatId));
        msg.setParseMode("Markdown");
        bot.send(msg);
    }

    public void myBooks(Message message) {
        User user = UserMapper.getInstance().getByChatId(message.getChatId().toString());
        message.setText(message.getChatId().toString());
        SearchService.getInstance().showResults(message, user);
    }

    public static BookService getInstance() {
        return instance;
    }
}
