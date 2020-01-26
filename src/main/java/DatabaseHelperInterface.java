import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseHelperInterface {
    void executeInsert(String query) throws SQLException;
    ResultSet executeSelect(String query) throws SQLException;
    void executeUpdate(String query) throws SQLException;
    void closeSelect() throws SQLException;
}
