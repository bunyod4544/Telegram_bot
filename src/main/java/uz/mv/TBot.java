package uz.mv;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mv.config.PConfig;
import uz.mv.entity.User;
import uz.mv.handles.CallBackHandle;
import uz.mv.handles.MsgHandle;
import uz.mv.mappers.UserMapper;
import uz.mv.repository.LogRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Amonov Bunyod, пт 24.12.2021 14:22 .
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TBot extends TelegramLongPollingBot {
    private Connection connection;
    public User sessionUser ;
    @Override
    public void onUpdateReceived(Update update) {
        LogRepository.getInstance().save(update);
        if (update.hasCallbackQuery()) {
            CallBackHandle.handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            MsgHandle.handleMessage(update.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return PConfig.get("bot.username");
    }

    @Override
    public String getBotToken() {
        return PConfig.get("bot.token");
    }


    public void send(BotApiMethod<?> obj) {
        try {
            execute(obj);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(SendDocument document) {
        try {
            execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(PConfig.get("jdbc.db"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static TBot instance = new TBot();

    public static TBot getInstance() {
        return instance;
    }

}
