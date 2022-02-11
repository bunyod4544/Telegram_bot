package uz.mv.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mv.config.PConfig;

import java.awt.image.PackedColorModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralRepository extends AbstractRepository{
    private static GeneralRepository instance = new GeneralRepository();

    public static GeneralRepository getInstance() {
        return instance;
    }


    public void saveNextPrevious(String code, int page, String searchingText) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.save.next.previous"));
        try{
            prst.setString(1,code);
            prst.setInt(2,page);
            prst.setString(3,searchingText);
            prst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getNextPrevious(String data) {
        PreparedStatement prst = getPreparedStatement(PConfig.get("query.get.next.previous"));
        try {
            prst.setString(1,data);
            return prst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getTopBooks(int page) {
        PreparedStatement prst = this.getPreparedStatement(PConfig.get("query.select.top"));

        try {
            prst.setInt(1, (page - 1) * 5);
            return prst.executeQuery();
        } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
        }
    }
}
