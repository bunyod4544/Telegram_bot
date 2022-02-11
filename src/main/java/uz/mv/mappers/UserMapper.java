package uz.mv.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mv.entity.User;
import uz.mv.enums.State;
import uz.mv.enums.langSettings.Language;
import uz.mv.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public User getByChatId(String chatId) {
        ResultSet resultSet = UserRepository.getInstance().getUserResultSetByChatId(chatId);
        try {
            if (resultSet.next()) {

                String chat_id = resultSet.getString("chat_id");
                String name = resultSet.getString("name");
                String full_name = resultSet.getString("full_name");
                String username = resultSet.getString("username");
                String phone_number = resultSet.getString("phone_number");
                String language = resultSet.getString("language");
                String state = resultSet.getString("state");
                String sessionBookId = resultSet.getString("session_book_id");
                String searchState = resultSet.getString("search_state");
                return User.builder()
                        .chatId(chat_id)
                        .username(username)
                        .name(name)
                        .fullName(full_name)
                        .phoneNumber(phone_number)
                        .language(Language.getByCode(language))
                        .state(State.getState(state))
                        .sessionBookId(sessionBookId)
                        .searchState(State.getState(searchState))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static UserMapper instance = new UserMapper();

    public static UserMapper getInstance() {
        return instance;
    }

}
