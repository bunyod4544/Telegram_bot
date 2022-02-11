package uz.mv.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mv.entity.Book;
import uz.mv.repository.BookRepository;
import uz.mv.repository.GeneralRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper {
    private static final BookMapper instance = new BookMapper();

    public static BookMapper getInstance() {
        return instance;
    }

    public List<Book> getBooksByKeyword(String text, int page) {
        List<Book> books = new ArrayList<>(5);
        ResultSet result = BookRepository.getInstance().getByKeyword(text, page);
        try {
            while (result.next()){
                books.add(getBookByFileId(result.getString("book_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Book getBookByFileId(String fileId) {
        ResultSet result = BookRepository.getInstance().getByFileId(fileId);
        try {
            if (result.next()) {
                int id = result.getInt("id");
                return getBook(id, result, fileId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getBookById(int id) {
        ResultSet result = BookRepository.getInstance().getById(id);
        try {
            if (result.next()) {
                String  fileId = result.getString("file_id");
                return getBook(id, result, fileId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Book getBook(int id, ResultSet result, String fileId) throws SQLException {
        String ownerId = result.getString("owner_id");
        String name = result.getString("name");
        String author = result.getString("author");
        String type = result.getString("type");
        String size = result.getString("size");
        int downloadCount = result.getInt("download_count");
        return Book.builder()
                .fileId(fileId)
                .id(id)
                .size(size)
                .ownerId(ownerId)
                .name(name)
                .author(author)
                .type(type)
                .downloadCount(downloadCount)
                .build();
    }

    public List<Book> getTopBooks(int page) {
        List<Book> books = new ArrayList(5);
        ResultSet result = GeneralRepository.getInstance().getTopBooks(page);

        try {
            while(result.next()) {
                books.add(this.getBookByFileId(result.getString("file_id")));
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        return books;
    }
}
