import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class KeyHistoryController extends BaseController {

    public TableView tblHistory ;
    public TableColumn colKeyIssueDate;
    public TableColumn colKeyIssueTime;
    public TableColumn colKeyReturnDate;
    public TableColumn colKeyReturnTime;
    public TableColumn colUserLastName;
    public TableColumn colUserFirstName;

    @Override
    public void OnLoad() {
        if (colKeyIssueDate != null) colKeyIssueDate.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        if (colKeyIssueTime != null) colKeyIssueTime.setCellValueFactory(new PropertyValueFactory<>("rentalTime"));
        if (colKeyReturnDate != null) colKeyReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        if (colKeyReturnTime != null) colKeyReturnTime.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
        if (colUserLastName != null) colUserLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        if (colUserFirstName != null) colUserFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    }

    @Override
    public void OnShowWithInt(boolean clean, int value) {
        ArrayList<KeyHistoryData> history = null;
        try {
            KeyHistoryData keyHistoryData = new KeyHistoryData(getDatabaseHelper());
            history = keyHistoryData.getKeyHistory(value);
            if (tblHistory != null) {
                tblHistory.getItems().clear();

                for (KeyHistoryData data : history) {
                    tblHistory.getItems().add(data);
                }
            }
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }

    }

    public void btnBack_OnAction(ActionEvent actionEvent) {
        getSceneManager().showScene(SceneManager.SceneType.RentalKeyHistory, false);
    }
}
