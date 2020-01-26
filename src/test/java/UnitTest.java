import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitTest {

    @BeforeEach
    public void initDataForAccount() {
    }

    @Test
    public void checkCorrectFormats() {
        assertTrue(KeyData.isKeyNumberFormatValid("101"));
        assertTrue(KeyData.isKeyNumberFormatValid("201"));
        assertTrue(KeyData.isKeyNumberFormatValid("301"));
        assertTrue(KeyData.isKeyNumberFormatValid("001"));
    }

    @Test
    public void checkIncorectFormats() {
        assertFalse(KeyData.isKeyNumberFormatValid("adc"));
    }

    @Test
    public void findKeyGlobalDataTest() {
        GlobalData globalData = GlobalData.instance;
        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("", null);
        ArrayList<KeyData> keysData = new ArrayList<>();
        keysData.add(new KeyData(databaseHelperMock, "rfid1", "101", "testowy1", 1, 4));
        keysData.add(new KeyData(databaseHelperMock, "rfid2", "201", "testowy2", 2, 5));
        keysData.add(new KeyData(databaseHelperMock, "rfid3", "102", "testowy3", 3, 6));

        globalData.setKeys(keysData);

        assertEquals(globalData.findKey("rfid1").getKeyId(), 4);

        KeyData nullKeyDataResult = globalData.findKey("UnknownRfidNumber");
        assertEquals(nullKeyDataResult, null);
    }

    @Test
    public void findUserGlobalDataTest() {
        GlobalData globalData = GlobalData.instance;
        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("", null);
        ArrayList<UserData> usersData = new ArrayList<>();
        usersData.add(new UserData(databaseHelperMock, 1, "imie1", "nazwisko1", "rfid1"));
        usersData.add(new UserData(databaseHelperMock, 2, "imie2", "nazwisko2", "rfid2"));
        usersData.add(new UserData(databaseHelperMock, 3, "imie3", "nazwisko3", "rfid3"));

        globalData.setUsers(usersData);

        assertEquals(globalData.findUser("rfid2").getIdUser(), 2);

        UserData userData = globalData.findUser("UnknownRfidNumber");
        assertEquals(userData, null);
    }

    @Test
    public void storeControllerSceneManagerTest() {
        SceneManager sceneManager = new SceneManager();
        TestController testController = new TestController();

        sceneManager.storeController(SceneManager.SceneType.Menu, testController);

        assertTrue(testController.isOnLoadCalled());
    }

    @Test
    public void showSceneSceneManagerTest() {
        SceneManager sceneManager = new SceneManager();
        TestController testController = new TestController();
        sceneManager.storeController(SceneManager.SceneType.Menu, testController);

        sceneManager.showScene(SceneManager.SceneType.Menu, true);

        assertTrue(testController.isOnShowCalled());
    }

    @Test
    public void addNewRoomTypeTest() {
        String roomTypeName = "TestRoomType";
        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("INSERT INTO \"RoomType\" (\"name\") VALUES ('" + roomTypeName + "');", null);
        RoomTypeData roomTypeData = new RoomTypeData(databaseHelperMock);
        try {
            roomTypeData.addNewRoomType(roomTypeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(databaseHelperMock.checkExpectedQuery());
    }

    @Test
    public void loadRoomTypesTest() {
        TestResultSet testResultSet = new TestResultSet();
        testResultSet.setRowsNumber(3);
        try {
            testResultSet.setStringField("name", new ArrayList<String>() {{
                add("test1");
                add("test2");
                add("test3");
            }});
            testResultSet.setIntField("idRoomType", new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("SELECT * FROM \"RoomType\";", testResultSet);
        RoomTypeData roomTypeData = new RoomTypeData(databaseHelperMock);
        ArrayList<RoomTypeData> result;
        try {
            result = roomTypeData.loadRoomTypes();

            assertEquals(testResultSet.getIntFromRow("idRoomType",1), result.get(1).getIdRoom());
            assertEquals(testResultSet.getStringFromRow("name", 2), result.get(2).getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(databaseHelperMock.checkExpectedQuery());
        assertTrue(databaseHelperMock.isSelectClosed(), "Forgot to close select query");
    }



    @Test
    public void addNewEmployeeTypeTest() {
        String employeeTypeName = "EmployeeType";
        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("INSERT INTO \"UserType\" (\"name\") VALUES ('" + employeeTypeName + "');", null);
        UserTypeData userTypeData = new UserTypeData(databaseHelperMock);
        try {
            userTypeData.addNewEmployeeType(employeeTypeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(databaseHelperMock.checkExpectedQuery());
    }

    @Test
    public void getEmployeeTypesTest() {
        TestResultSet testResultSet = new TestResultSet();
        testResultSet.setRowsNumber(2);
        try {
            testResultSet.setStringField("name", new ArrayList<String>() {{
                add("test1");
                add("test2");
            }});
            testResultSet.setIntField("idUserType", new ArrayList<Integer>() {{
                add(1);
                add(2);
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("SELECT * FROM \"UserType\";", testResultSet);
        UserTypeData userTypeData = new UserTypeData(databaseHelperMock);
        ArrayList<UserTypeData> result;
        try {
            result = userTypeData.getEmployeeTypes();

            assertEquals(testResultSet.getIntFromRow("idUserType",0), result.get(0).getId());
            assertEquals(testResultSet.getStringFromRow("name",1), result.get(1).getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(databaseHelperMock.checkExpectedQuery());
        assertTrue(databaseHelperMock.isSelectClosed(), "Forgot to close select query");
    }


    @Test
    public void showSceneRxtxManagerTest() {
        LoginController loginController = new LoginController();
        SceneManager sceneManager = new SceneManager();

        assertTrue(!RxTxManager.instance.isCommandSet());

        sceneManager.storeController(SceneManager.SceneType.Login, loginController);
        sceneManager.showScene(SceneManager.SceneType.Login, true);

        assertTrue(RxTxManager.instance.isCommandSet());
    }

    @Test
    public void showSceneWithValueDatabaseTest() {
        int keyIdValue = 1;
        TestResultSet emptyResultSet = new TestResultSet();
        DatabaseHelperMock databaseHelperMock = new DatabaseHelperMock("SELECT * FROM \"RentalHistory\" NATURAL JOIN \"User\" WHERE \"idKey\" = " + keyIdValue + ";", emptyResultSet);

        KeyHistoryController keyHistoryController = new KeyHistoryController();
        keyHistoryController.setDatabaseHelper(databaseHelperMock);

        SceneManager sceneManager = new SceneManager();
        sceneManager.storeController(SceneManager.SceneType.KeyHistory, keyHistoryController);

        sceneManager.showSceneWithIntValue(SceneManager.SceneType.KeyHistory, true, keyIdValue);

        assertTrue(databaseHelperMock.checkExpectedQuery());
        assertTrue(databaseHelperMock.isSelectClosed(), "Forgot to close select query");
    }
}
