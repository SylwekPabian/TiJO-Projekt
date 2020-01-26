import java.sql.SQLException;
import java.util.ArrayList;

public class GlobalData {
    public static final GlobalData instance = new GlobalData();

    private ArrayList<UserData> users = new ArrayList<>();
    private ArrayList<KeyData> keys = new ArrayList<>();

    private GlobalData() {}

    public void loadUsers() throws SQLException {
        UserData userData = new UserData(DatabaseHelper.insatnce);
        users = userData.loadUsers();
    }

    public void loadKeys() throws SQLException {
        KeyData keyData = new KeyData(DatabaseHelper.insatnce);
        keys = keyData.loadKeys();
    }

    public UserData findUser(String rfidNumber) {
        for (UserData employee : users) {
            if (employee.getRfidNumber().equals(rfidNumber)) {
                return employee;
            }
        }
        return null;
    }

    public KeyData findKey(String rfidNumber) {
        for (KeyData key : keys) {
            if (key.getRfidNumber().equals(rfidNumber)) {
                return key;
            }
        }
        return null;
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserData> userData) {
        users = userData;
    }

    public ArrayList<KeyData> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<KeyData> keyData) {
        keys = keyData;
    }
}
