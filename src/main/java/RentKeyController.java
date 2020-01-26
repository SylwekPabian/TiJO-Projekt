import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class RentKeyController extends BaseController {

    public TextField txtBarcodeNumber;
    public TextField txtIdNumber;
    public TextField txtLastName;
    public TextField txtFirstName;

    private class RfidReaderCommand implements RxTxManager.Command {
        @Override
        public void execute(String data) {
            processScannedId(data);
        }
    }

    @Override
    public void OnLoad() {
        super.OnLoad();

        txtIdNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            UserData user = GlobalData.instance.findUser(newValue);
            if (user != null) {
                txtFirstName.setText(user.getFirstName());
                txtLastName.setText(user.getLastName());
            } else {
                txtFirstName.setText("");
                txtLastName.setText("");
            }
        });
    }

    @Override
    public void OnShow(boolean clean) {
        super.OnShow(clean);

        if (clean) {
            txtFirstName.setText("");
            txtLastName.setText("");
            txtIdNumber.setText("");
            txtBarcodeNumber.setText("");
        }

        try {
            RxTxManager.instance.enableRfidReader(new RfidReaderCommand());
        } catch (Exception e) {
            getSceneManager().showErrorAlert("Wystąpiła problem z czytnikiem RFID!");
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
            txtIdNumber.setText(scannedData);
            return;
        }

        KeyData key = GlobalData.instance.findKey(scannedData);
        if (key != null) {
            txtBarcodeNumber.setText(scannedData);
            return;
        }

        System.out.println("Nieznany identyfikator");
        //getSceneManager().showInfoAlert("Nieznany identyfikator!!");
    }

    public void btnBack_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }

    public void btnRent_OnAction(ActionEvent actionEvent) {

        try {
            String userIdTxt = txtIdNumber.getText();
            String barcodeNumberTxt = txtBarcodeNumber.getText();
            UserData userData = GlobalData.instance.findUser(userIdTxt);
            KeyData keyData = GlobalData.instance.findKey(barcodeNumberTxt);
            RentalHistoryData rentalHistoryData = new RentalHistoryData(DatabaseHelper.insatnce);
            if (!keyData.isAvailable(keyData.getKeyId())) {
                getSceneManager().showInfoAlert("Klucz jest wypożyczony!!");
                return;
            }
            if (userData != null) {
                rentalHistoryData.saveKeyRentalToDatabase(userData.getIdUser(), keyData.getKeyId());
                getSceneManager().showScene(SceneManager.SceneType.Menu, true);
            } else {
                if (txtIdNumber.getText().isEmpty() || txtLastName.getText().isEmpty() || txtFirstName.getText().isEmpty()) {
                    getSceneManager().showInfoAlert("Wszystkie pola muszą być uzupełnione!!!");
                } else {
                    String idNumber = txtIdNumber.getText();
                    String lastName = txtLastName.getText();
                    String firstName = txtFirstName.getText();
                    rentalHistoryData.rentToNewUser(keyData.getKeyId(), idNumber, lastName, firstName);
                    getSceneManager().showScene(SceneManager.SceneType.Menu, true);
                }
            }
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }
}
