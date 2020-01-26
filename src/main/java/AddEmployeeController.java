import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;

public class AddEmployeeController extends BaseController {
    public TextField txtFirstName;
    public TextField txtLastName;
    public ListView lstEmployeeType;
    public TextField txtRfidNumber;
    public TextField txtNewEmployeeType;


    UserData userData = new UserData(DatabaseHelper.insatnce);
    ArrayList<UserTypeData> employeTypes;

    private class RfidReaderCommand implements RxTxManager.Command {
        @Override
        public void execute(String data) {
            txtRfidNumber.setText(data);
        }
    }

    @Override
    public void OnLoad() {
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            userData.setFirstName(newValue);
        });
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
            userData.setLastName(newValue);
        });
        txtRfidNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            userData.setRfidNumber(newValue);
        });
    }

    @Override
    public void OnShow(boolean clean) {
        if (clean) {
            userData = new UserData(DatabaseHelper.insatnce);
            fillEmployeeTypeList();

            txtFirstName.setText("");
            txtLastName.setText("");
            txtRfidNumber.setText("");
            txtNewEmployeeType.setText("");
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

    private void fillEmployeeTypeList() {
        try {
            UserTypeData userTypeData = new UserTypeData(DatabaseHelper.insatnce);
            employeTypes = userTypeData.getEmployeeTypes();
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }

        lstEmployeeType.getItems().clear();
        for (UserTypeData et : employeTypes) {
            CheckBox checkbox = new CheckBox(et.getName());
            checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    et.setChecked(newValue);
            });

            lstEmployeeType.getItems().add(checkbox);
        }
    }

    public void btnBack_OnAction(ActionEvent actionEvent) {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        try {
            userData.saveInDatabase(employeTypes);
            getSceneManager().showScene(SceneManager.SceneType.Menu, true);
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }

    public void btnAddNewEmployeeType_OnAction(ActionEvent actionEvent) {
        try {
            UserTypeData userTypeData = new UserTypeData(DatabaseHelper.insatnce);
            if (userTypeData.isEmployeeTypeInDatabase(txtNewEmployeeType.getText())) {
                userTypeData.addNewEmployeeType(txtNewEmployeeType.getText());
                txtNewEmployeeType.setText("");
                fillEmployeeTypeList();
            } else {
                getSceneManager().showInfoAlert("W bazie danych znajduje się już taki typ pracownika!!!!");
            }
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }
    }
}
