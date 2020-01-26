import java.sql.SQLException;

public class RentalHistoryData {
    private DatabaseHelperInterface databaseHelper;

    public RentalHistoryData(DatabaseHelperInterface helper) {
        databaseHelper = helper;
    }

    public void saveKeyRentalToDatabase(int idUser, int idKey) throws SQLException {
        String query = "INSERT INTO \"RentalHistory\" (\"idUser\", \"idKey\", \"rentalDate\", \"rentalTime\") " +
                "VALUES (" + idUser + ", " +
                idKey + ", " +
                "(SELECT current_date)," +
                "(SELECT current_time));";

        databaseHelper.executeInsert(query);
    }

    public void saveKeyReturnToDatabase(int idUser, int idKey) throws SQLException {
        String query = "UPDATE \"RentalHistory\" " +
                "SET \"returnDate\" = (SELECT current_date), " +
                "\"returnTime\" = (SELECT current_time) " +
                "WHERE \"idUser\" = " + idUser + "  AND \"idKey\" = " + idKey + ";";
        databaseHelper.executeUpdate(query);
    }

    public void rentToNewUser(int idKey, String idNumber, String lastName, String firstName) throws SQLException {
        String query = "INSERT INTO \"User\" (\"lastName\", \"firstName\", \"rfidNumber\") VALUES ('"
                + lastName + "', '"
                + firstName + "', '"
                + idNumber + "'); \n"
                + "INSERT INTO \"RentalHistory\" (\"idUser\", \"idKey\", \"rentalDate\", \"rentalTime\") " +
                "VALUES (" + "(SELECT \"idUser\" FROM \"User\" WHERE \"rfidNumber\" = '" + idNumber + "' )," +
                idKey + ", " +
                "(SELECT current_date)," +
                "(SELECT current_time));";
        /*query += "INSERT INTO \"RentalHistory\" (\"idUser\", \"idKey\", \"rentalDate\", \"rentalTime\") " +
                "VALUES (" + "(SELECT \"idUser\" FROM \"User\" WHERE \"rfidNumber\" = '" + idNumber + "' )," +
                idKey + ", " +
                "(SELECT current_date)," +
                "(SELECT current_time));";*/

        databaseHelper.executeInsert(query);
        GlobalData.instance.loadUsers();
    }
}
