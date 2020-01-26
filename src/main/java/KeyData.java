import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KeyData {
    DatabaseHelperInterface databaseHelper;

    private String rfidNumber;
    private String roomNumber;
    private String roomGroup;
    private int roomGroupId;
    private int keyId;

    public KeyData(DatabaseHelperInterface helper) {
        databaseHelper = helper;
    }
    public KeyData(DatabaseHelperInterface helper, String rfidNumber, String roomNumber, String roomGroup, int roomGroupId, int keyId) {
        databaseHelper = helper;

        this.rfidNumber = rfidNumber;
        this.roomNumber = roomNumber;
        this.roomGroup = roomGroup;
        this.roomGroupId = roomGroupId;
        this.keyId = keyId;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomGroup() {
        return roomGroup;
    }

    public void setRoomGroup(String roomGroup) {
        this.roomGroup = roomGroup;
    }

    public int getRoomGroupId() {
        return roomGroupId;
    }

    public void setRoomGroupId(int roomGroupId) {
        this.roomGroupId = roomGroupId;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public static boolean isKeyNumberFormatValid(String keyNumber) {
        if (keyNumber.length() != 3 || !isNumeric(keyNumber)) {
            return false;
        }
        return true;
    }

    private static boolean isNumeric(String keyNumber) {
        if (keyNumber == null) {
            return false;
        }
        try {
            int intKeyNumber = Integer.parseInt(keyNumber);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void saveToDatabase() throws SQLException {
        String query = "INSERT INTO \"Key\" (\"rfidNumber\", \"roomNumber\") VALUES ('" + rfidNumber + "', '" + roomNumber + "');" +
                "INSERT INTO \"KeyRoomType\" (\"idKey\", \"idRoomType\")" +
                "VALUES" +
                "((SELECT \"idKey\" FROM \"Key\" WHERE \"rfidNumber\" = '" + rfidNumber + "'), " +
                "" + roomGroupId + ");";

        databaseHelper.executeInsert(query);
    }

    public ArrayList<KeyData> loadKeys() throws SQLException {
        ArrayList<KeyData> keys = new ArrayList<>();

        String query = "SELECT \"idKey\", \"roomNumber\", \"name\", \"idRoomType\", \"rfidNumber\" FROM \"Key\" NATURAL JOIN \"KeyRoomType\" NATURAL JOIN \"RoomType\";";
        ResultSet resultSet = databaseHelper.executeSelect(query);

        while (resultSet != null && resultSet.next()) {
            KeyData key = new KeyData(databaseHelper);
            key.setKeyId(resultSet.getInt("idKey"));
            key.setRoomNumber(resultSet.getString("roomNumber"));
            key.setRoomGroup(resultSet.getString("name"));
            key.setRoomGroupId(resultSet.getInt("idRoomType"));
            key.setRfidNumber(resultSet.getString("rfidNumber"));
            keys.add(key);
        }

        databaseHelper.closeSelect();

        return keys;
    }

    public boolean isAvailable (int idKey) throws SQLException {
        boolean result = true;
        String query = "SELECT * FROM \"RentalHistory\" WHERE \"idKey\" = " + idKey + " AND \"rentalDate\" IS NOT NULL AND \"returnDate\" IS NULL;";

        ResultSet resultSet = databaseHelper.executeSelect(query);
        if (resultSet.next()) {
            result = false;
        }
        databaseHelper.closeSelect();
        return result;
    }

    public boolean isRentedTo (int idUser, int idKey) throws SQLException {
        boolean result = false;
        String query = "SELECT * FROM \"RentalHistory\" WHERE \"idUser\" = " + idUser + " AND \"idKey\" = " + idKey + " AND \"returnDate\" IS NULL AND \"returnTime\" IS NULL;";

        ResultSet resultSet = databaseHelper.executeSelect(query);
        if (resultSet.next()) {
            result = true;
        }
        databaseHelper.closeSelect();
        return result;
    }
}
