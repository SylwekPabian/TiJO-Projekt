import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddKeyController extends BaseController {

    public TextField txtRfidNumber;
    public TextField txtRoomNumber;
    public ChoiceBox chbRoomType;
    public TextField txtNewRoomType;

    KeyData keyData = new KeyData(DatabaseHelper.insatnce);
    ArrayList<RoomTypeData> roomTypes = new ArrayList<>();

    private class RfidReaderCommand implements RxTxManager.Command {
        @Override
        public void execute(String data) {
            txtRfidNumber.setText(data);
        }
    }

    @Override
    public void OnLoad() {
        txtRfidNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            keyData.setRfidNumber(newValue);
        });
        txtRoomNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            keyData.setRoomNumber(newValue);
        });
        chbRoomType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int roomTypeId = RoomTypeData.findRoomTypeId(roomTypes, newValue.toString());
                if (roomTypeId != -1) {
                    keyData.setRoomGroupId(roomTypeId);
                }
                else {
                    System.out.println("Nie powinno się zdarzyć, ale nie znaleźliśmy roomTypeId");
                }
            }
        });
    }

    @Override
    public void OnShow(boolean clean) {
        super.OnShow(clean);
        if (clean) {
            keyData = new KeyData(DatabaseHelper.insatnce);

            txtRoomNumber.setText("");
            txtRfidNumber.setText("");

            fillRoomTypes();
        }

        try {
            RxTxManager.instance.enableRfidReader(new RfidReaderCommand());
        } catch (Exception e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void OnClose() {
        super.OnClose();
        RxTxManager.instance.disableRfidReader();
    }

    private void fillRoomTypes() {
        chbRoomType.getItems().clear();
        try {
            RoomTypeData roomTypeData = new RoomTypeData(DatabaseHelper.insatnce);
            roomTypes = roomTypeData.loadRoomTypes();
            for (RoomTypeData roomType : roomTypes) {
                chbRoomType.getItems().add(roomType.getName());
            }
            if (!roomTypes.isEmpty()) chbRoomType.getSelectionModel().select(0);
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }

    public void btnBack_OnAction() throws IOException {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }

    public void btnAdd_OnAction() throws IOException {
        if (KeyData.isKeyNumberFormatValid(keyData.getRoomNumber())) {
            try {
                keyData.saveToDatabase();
                GlobalData.instance.loadKeys();
                getSceneManager().showScene(SceneManager.SceneType.Menu, true);
            } catch (SQLException e) {
                getSceneManager().showErrorAlert(e.toString());
                e.printStackTrace();
            }
        }
        else {
            getSceneManager().showInfoAlert("Niepoprawny format numeru sali!!!");
        }
    }

    public void btnAddNewRoomType_OnAction(ActionEvent actionEvent) {
        String newRoomType = txtNewRoomType.getText();
        if (!newRoomType.isEmpty()) {
            try {
                RoomTypeData roomTypeData = new RoomTypeData(DatabaseHelper.insatnce);
                if (!roomTypeData.isTypeNameInDatabase(newRoomType)) {
                    getSceneManager().showInfoAlert("Ten typ sali znajduje się już w bazie danych!");
                    return;
                }
                roomTypeData.addNewRoomType(newRoomType);
                txtNewRoomType.setText("");
                fillRoomTypes();
            } catch (SQLException e) {
                getSceneManager().showErrorAlert(e.toString());
                e.printStackTrace();
            }
        }
    }
}
