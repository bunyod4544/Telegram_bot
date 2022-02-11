package uz.mv;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.mv.enums.State;

/**
 * @author Amonov Bunyod, пт 24.12.2021 12:10 .
 */
public class App {
    public static void main(String[] args) {
        try {
            System.out.println("BOTTTT");
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(TBot.getInstance());
            System.out.println("STOP");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
