package uz.mv.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mv.config.PConfig;
import uz.mv.entity.Book;

import java.sql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookRepository extends AbstractRepository {
    private static BookRepository instance;

    public static BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public void save(Book book) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.save.book"));
        try {
            //file_id, owner_id, name, type, size, download_count
            setBookFields(book, prst);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer save(String data, String chatId) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(PConfig.get("users.update.query").formatted(data, chatId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveKeywords(String fileId, String keyword) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.save.keywords"));
        try {
            prst.setString(1, keyword);
            prst.setString(2, fileId);
            prst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param text searching text
     * @param page page number
     * @return ResultSet of BookFileIDs
     */
    public ResultSet getByKeyword(String text, int page) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.select.books.ids"));
        try {
            prst.setString(1, "%" + text + "%");
            prst.setInt(2, (page - 1) * 5);
            return prst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getByFileId(String fileId) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.select.book"));
        try {
            prst.setString(1, fileId);
            return prst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getById(int id) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.select.book.id"));
        PreparedStatement download = getPreparedStatement(PConfig.get("query.set.download.count"));
        try {
            prst.setInt(1, id);
            download.setInt(1, id);
            download.executeUpdate();
            return prst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBook(Book book) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.update.book"));
        try {
            setBookFields(book, prst);
            prst.setInt(8, book.getId());
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setBookFields(Book book, PreparedStatement prst) throws SQLException {
        prst.setString(1, book.getFileId());
        prst.setString(2, book.getOwnerId());
        prst.setString(3, book.getName());
        prst.setString(4, book.getType());
        prst.setString(5, book.getSize());
        prst.setInt(6, book.getDownloadCount());
        prst.setString(7, book.getAuthor());
    }
}
