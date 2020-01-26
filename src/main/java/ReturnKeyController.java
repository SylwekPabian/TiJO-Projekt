import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class ReturnKeyController extends BaseController {
    public TextField txtFirstName;
    public TextField txtLastName;
    public Label barcodeKeyLabel;
    public Label userIdLabel;
    public TextField txtBarcodeKey;
    public TextField txtUserId;

    private class RfidReaderCommand implements RxTxManager.Command {
        @Override
        public void execute(String data) {
            processScannedId(data);
        }
    }

    @Override
    public void OnLoad() {
        super.OnLoad();

        txtUserId.textProperty().addListener((observable, oldValue, newValue) -> {
            UserData user = GlobalData.instance.findUser(newValue);
            if (user != null) {
                txtFirstName.setText(user.getFirstName());
                txtLastName.setText(user.getLastName());
            }
        });
    }

    @Override
    public void OnShow(boolean clean) {
        super.OnShow(clean);

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

    private void processScannedId(String scannedData) {
        UserData user = GlobalData.instance.findUser(scannedData);
        if (user != null) {
            txtUserId.setText(scannedData);
            return;
        }

        KeyData key = GlobalData.instance.findKey(scannedData);
        if (key != null) {
            txtBarcodeKey.setText(scannedData);
            return;
        }

        System.out.println("Nieznany identyfikator");
    }

    public void btnCancel_OnAction() throws IOException {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }

    public void btnEnd_OnAction() throws IOException, SQLException {
        String userIdTxt = txtUserId.getText();
        int userId = GlobalData.instance.findUser(userIdTxt).getIdUser();
        String barcodeNumberTxt = txtBarcodeKey.getText();
        int barcodeNumber = GlobalData.instance.findKey(barcodeNumberTxt).getKeyId();
        KeyData keyData = GlobalData.instance.findKey(barcodeNumberTxt);
        if (keyData.isAvailable(barcodeNumber)){
            getSceneManager().showInfoAlert("Klucz nie został wypożyczony!!!");
            return;
        }
        if (!keyData.isRentedTo(userId, barcodeNumber)) {
            getSceneManager().showInfoAlert("Ten klucz nie został wypożyczony temu użytkownikowi!!!!");
            return;
        }
        try {
            RentalHistoryData rentalHistoryData = new RentalHistoryData(DatabaseHelper.insatnce);
            rentalHistoryData.saveKeyReturnToDatabase(userId, barcodeNumber);
            getSceneManager().showScene(SceneManager.SceneType.Menu, true);
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }
}
