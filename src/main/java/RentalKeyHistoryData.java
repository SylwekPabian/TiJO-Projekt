import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.sql.SQLException;
import java.util.ArrayList;

public class RentalKeyHistoryData {
    private int idKey;
    private String roomNumber;
    private String roomGroup;
    private Button moreInfo;

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

    public Button getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(Button moreInfo) {
        this.moreInfo = moreInfo;
    }

    public int getIdKey() {
        return idKey;
    }

    public void setIdKey(int idKey) {
        this.idKey = idKey;
    }

    public static ArrayList<RentalKeyHistoryData> getKeyList(SceneManager sceneManager) throws SQLException {
        ArrayList<RentalKeyHistoryData> history = new ArrayList<>();

        for (KeyData keyData: GlobalData.instance.getKeys()) {
            RentalKeyHistoryData key = new RentalKeyHistoryData();
            key.setIdKey(keyData.getKeyId());
            key.setRoomNumber(keyData.getRoomNumber());
            key.setRoomGroup(keyData.getRoomGroup());
            Button btn = new Button("Historia");
            btn.setOnAction((ActionEvent actionEvent) -> {sceneManager.showSceneWithIntValue(SceneManager.SceneType.KeyHistory, true, keyData.getKeyId());});
            btn.setPrefWidth(300.0);
            key.setMoreInfo(btn);
            history.add(key);
        }
        return history;
    }
}
