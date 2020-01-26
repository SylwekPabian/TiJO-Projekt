import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelperMock implements DatabaseHelperInterface {
    private String expectedQuery;
    private String receivedQuery;
    private ResultSet returnResultSet;
    private boolean selectClosed;

    public DatabaseHelperMock(String query, ResultSet resultSet) {
        expectedQuery = query;
        returnResultSet = resultSet;
    }

    public boolean checkExpectedQuery() {
        return expectedQuery.equals(receivedQuery);
    }

    public ResultSet getReturnedResultSet() {
        return returnResultSet;
    }

    public boolean isSelectClosed() {
        return selectClosed;
    }

    @Override
    public void executeInsert(String query) throws SQLException {
        selectClosed = false;
        receivedQuery = query;
    }

    @Override
    public ResultSet executeSelect(String query) throws SQLException {
        selectClosed = false;
        receivedQuery = query;
        return returnResultSet;
    }

    @Override
    public void executeUpdate(String query) throws SQLException {
        selectClosed = false;
        receivedQuery = query;
    }

    @Override
    public void closeSelect() throws SQLException {
        selectClosed = true;
    }
}
