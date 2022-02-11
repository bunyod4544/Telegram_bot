package uz.mv.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import uz.mv.TBot;
import uz.mv.config.PConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Getter
public abstract class AbstractRepository {
    protected Connection connection = getConnection();
    protected Gson gson = new Gson();

    public Connection getConnection() {
        return TBot.getInstance().getConnection();
    }

    protected PreparedStatement getPreparedStatement(String query) {
        try {
            return getConnection().prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
