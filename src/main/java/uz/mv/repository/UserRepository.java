package uz.mv.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mv.config.PConfig;
import uz.mv.entity.User;

import java.sql.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepository extends AbstractRepository {

    public ResultSet getUserResultSetByChatId(String chatId) {
        try {
            PreparedStatement prst = getPreparedStatement(PConfig.get("query.get.user"));
            prst.setString(1, chatId);
            ResultSet resultSet = prst.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            PreparedStatement prst = getPreparedStatement(PConfig.get("query.save.user"));
            prst.setString(1, user.getChatId());
            prst.setString(2, user.getName());
            prst.setString(3, user.getFullName());
            prst.setString(4, user.getUsername());
            prst.setString(5, user.getPhoneNumber());
            prst.setString(6, user.getLanguage().getCode());
            prst.setString(7, user.getState().getName());
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement prst = getPreparedStatement(PConfig.get("query.update.user"));
            prst.setString(1, user.getName());
            prst.setString(2, user.getPhoneNumber());
            prst.setString(3, user.getState().getName());
            prst.setString(4, user.getSessionBookId());
            prst.setString(5, (Objects.nonNull(user.getSearchState()) ? user.getSearchState().getName() : "NONE"));
            prst.setString(6, user.getChatId());
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public String findLanguage(String chatId) {
//        try {
//            PreparedStatement prst = getPreparedStatement(PConfig.get("query.get.language").formatted(chatId));
//            return prst.getResultSet().getString("language");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String findLanguage(String chatId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("query.get.language").formatted(chatId));
            return resultSet.getString("language");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static UserRepository instance = new UserRepository();

    public static UserRepository getInstance() {
        return instance;
    }
}
