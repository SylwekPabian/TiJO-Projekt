import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserData {
    DatabaseHelperInterface databaseHelper;

    private int idUser;
    private String firstName = "";
    private String lastName = "";
    private String rfidNumber = "";
    private ArrayList<UserTypeData> types;

    public UserData(DatabaseHelperInterface helper) {
        databaseHelper = helper;
    }

    public UserData(DatabaseHelperInterface helper, int idUser, String firstName, String lastName, String rfidNumber) {
        databaseHelper = helper;

        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rfidNumber = rfidNumber;
        this.types = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public ArrayList<UserTypeData> getTypes() {
        return types;
    }

    public void saveInDatabase(ArrayList<UserTypeData> employeTypes) throws SQLException {
        String query = "INSERT INTO \"User\" (\"lastName\", \"firstName\", \"rfidNumber\") VALUES ('"
                + lastName + "', '" + firstName + "', '" + rfidNumber + "');\n";

        for (UserTypeData type : employeTypes) {
            if (type.isChecked()) {
                query += "INSERT INTO \"UserUserType\" (\"idUser\", \"idUserType\") " + "VALUES " +
                        "((SELECT \"idUser\" FROM \"User\" WHERE \"rfidNumber\" = '" + rfidNumber + "' )," +
                        "" + type.getId() + ");\n";
            }
        }
        databaseHelper.executeInsert(query);
    }

    public ArrayList<UserData> loadUsers() throws SQLException {
        ArrayList<UserData> employees = new ArrayList<>();

        String query = "SELECT * FROM \"User\";";

        ResultSet resultSet = databaseHelper.executeSelect(query);

        while (resultSet != null && resultSet.next()) {
            UserData employee = new UserData(databaseHelper);
            employee.setIdUser(resultSet.getInt("idUser"));
            employee.setLastName(resultSet.getString("lastName"));
            employee.setFirstName(resultSet.getString("firstName"));
            employee.setRfidNumber(resultSet.getString("rfidNumber"));
            employees.add(employee);
        }

        databaseHelper.closeSelect();

        return employees;
    }

    public boolean isPorter(int idUser) throws SQLException {
        boolean result = true;
        String queryUserTypes = "SELECT * FROM \"UserUserType\" NATURAL JOIN \"UserType\" WHERE \"name\" = 'Portier' AND \"idUser\" = " + idUser + ";";

        ResultSet resultSet = databaseHelper.executeSelect(queryUserTypes);
        if (!resultSet.next()) {
            result = false;
        }
        databaseHelper.closeSelect();
        return result;
    }
}
