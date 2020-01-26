import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KeyHistoryData {
    private DatabaseHelperInterface databaseHelper;

    private String rentalDate;
    private String rentalTime;
    private String returnDate;
    private String returnTime;
    private String lastName;
    private String firstName;

    public KeyHistoryData(DatabaseHelperInterface history) {
        databaseHelper = history;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getRentalTime() {
        return rentalTime;
    }

    public void setRentalTime(String rentalTime) {
        this.rentalTime = rentalTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public ArrayList<KeyHistoryData> getKeyHistory(int keyId) throws SQLException {
        ArrayList<KeyHistoryData> history = new ArrayList<>();

        String query = "SELECT * FROM \"RentalHistory\" NATURAL JOIN \"User\" WHERE \"idKey\" = " + keyId + ";";
        ResultSet resultSet = databaseHelper.executeSelect(query);

        while (resultSet.next()) {
            KeyHistoryData keyHistoryData = new KeyHistoryData(databaseHelper);
            keyHistoryData.setRentalDate(resultSet.getString("rentalDate"));
            keyHistoryData.setRentalTime(resultSet.getString("rentalTime").substring(0, 8));
            String returnDate = resultSet.getString("returnDate");
            String returnTime = resultSet.getString("returnTime");
            if (returnDate != null || returnTime != null ) {
                keyHistoryData.setReturnDate(returnDate);
                keyHistoryData.setReturnTime(returnTime.substring(0, 8));
            }
            keyHistoryData.setLastName(resultSet.getString("lastName"));
            keyHistoryData.setFirstName(resultSet.getString("firstName"));
            history.add(keyHistoryData);
        }

        databaseHelper.closeSelect();

        return history;
    }
}
