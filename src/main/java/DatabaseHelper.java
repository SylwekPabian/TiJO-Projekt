import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper implements DatabaseHelperInterface {
    public static DatabaseHelper insatnce = new DatabaseHelper();

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private DatabaseHelper() {}

    public void executeInsert(String query) throws SQLException {
        executeUpdate(query);
    }

    public ResultSet executeSelect(String query) throws SQLException {
        connection = ConnectionManager.getConnection();
        if (connection != null) {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }
        return resultSet;
    }

    public void executeUpdate(String query) throws SQLException {
        connection = ConnectionManager.getConnection();
        if (connection != null) {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }

        statement.close();
        statement = null;
        connection.close();
        connection = null;
    }

    public void closeSelect() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
            resultSet = null;
        }
        if (statement != null) {
            statement.close();
            statement = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
