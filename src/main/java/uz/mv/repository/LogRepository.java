package uz.mv.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.mv.config.PConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogRepository extends AbstractRepository {

    public void save(Update update) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("log.query"));
        try {
            if (update.hasMessage()) {
                prst.setString(1, update.getMessage().getChatId().toString());
                prst.setString(4, gson.toJson(update.getMessage().getFrom()));
                prst.setString(2, update.getMessage().getText());
                prst.setString(3, gson.toJson(update));
                prst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static LogRepository instance;

    public static LogRepository getInstance() {
        if (instance == null) {
            instance = new LogRepository();
        }
        return instance;
    }

}
