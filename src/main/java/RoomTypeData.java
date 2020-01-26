import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomTypeData {
    DatabaseHelperInterface databaseHelper;

    private int idRoom;
    private String name;

    public RoomTypeData(DatabaseHelperInterface helper) {
        databaseHelper = helper;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int findRoomTypeId(ArrayList<RoomTypeData> roomTypes, String searchedName) {
        int id = -1;
        for (RoomTypeData roomType : roomTypes) {
            if (roomType.name.equals(searchedName)) {
                return roomType.getIdRoom();
            }
        }
        return id;
    }

    public ArrayList<RoomTypeData> loadRoomTypes() throws SQLException {
        ArrayList<RoomTypeData> roomTypes = new ArrayList<>();

        String query = "SELECT * FROM \"RoomType\" ORDER BY \"name\";";
        ResultSet result = databaseHelper.executeSelect(query);

        while (result.next()) {
            RoomTypeData roomTypeData = new RoomTypeData(databaseHelper);
            roomTypeData.setIdRoom(result.getInt("idRoomType"));
            roomTypeData.setName(result.getString("name"));
            roomTypes.add(roomTypeData);
        }

        databaseHelper.closeSelect();
        return roomTypes;
    }

    public void addNewRoomType(String roomTypeName) throws SQLException {
        String query = "INSERT INTO \"RoomType\" (\"name\") VALUES ('" + roomTypeName + "');";
        databaseHelper.executeInsert(query);
    }

    public boolean isTypeNameInDatabase(String name) throws SQLException {
        boolean result = false;
        String query = "SELECT \"name\" FROM \"RoomType\" WHERE \"name\" = '" + name + "'";
        ResultSet rs = databaseHelper.executeSelect(query);
        if (!rs.next()) {
            result = true;
        }
        databaseHelper.closeSelect();
        return result;
    }
}
