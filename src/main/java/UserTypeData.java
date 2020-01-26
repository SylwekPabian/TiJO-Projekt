import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserTypeData {
    DatabaseHelperInterface databaseHelper;

    private int id;
    private String name;
    private boolean checked;

    public UserTypeData(DatabaseHelperInterface helper) {
        databaseHelper = helper;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ArrayList<UserTypeData> getEmployeeTypes() throws SQLException {

        ArrayList<UserTypeData> userTypeData = new ArrayList<>();

        String query = "SELECT * FROM \"UserType\" ORDER BY \"name\";";

        ResultSet rs = databaseHelper.executeSelect(query);

        while(rs.next()) {
            UserTypeData type = new UserTypeData(databaseHelper);
            type.setId(rs.getInt("idUserType"));
            type.setName(rs.getString("name"));
            type.setChecked(false);
            userTypeData.add(type);
        }

        databaseHelper.closeSelect();

        return userTypeData;
    }

    public void addNewEmployeeType(String employeeTypeName) throws SQLException {
        String query = "INSERT INTO \"UserType\" (\"name\") VALUES ('" + employeeTypeName + "');";
        databaseHelper.executeInsert(query);
    }

    public boolean isEmployeeTypeInDatabase (String employeeTypeName) throws SQLException {
        boolean result = false;
        String query = "SELECT \"name\" FROM \"UserType\" WHERE \"name\" = '" + employeeTypeName + "'";
        ResultSet rs = databaseHelper.executeSelect(query);
        if (!rs.next()) {
            result = true;
        }
        databaseHelper.closeSelect();
        return result;
    }
}
