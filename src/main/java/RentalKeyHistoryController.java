import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RentalKeyHistoryController extends BaseController {

    public TableView tblRoomList;
    public TableColumn colRoomNumber;
    public TableColumn colRoomType;
    public TableColumn colMoreInfo;

    RentalKeyHistoryData historyData = new RentalKeyHistoryData();
    ArrayList<RentalKeyHistoryData> history = new ArrayList<>();

    @Override
    public void OnLoad() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomGroup"));
        colMoreInfo.setCellValueFactory(new PropertyValueFactory<>("moreInfo"));
    }

    @Override
    public void OnShow(boolean clean) {
        fillRoomList(0);
    }

    private int getFloorFromRoomNumber(String roomNumber) {
        String floorNumber = Character.toString(roomNumber.charAt(0));
        return Integer.parseInt(floorNumber);
    }

    private void fillRoomList(int selectedFloor) {
        tblRoomList.getItems().clear();
        ArrayList<RentalKeyHistoryData> history = null;
        try {
            history = RentalKeyHistoryData.getKeyList(getSceneManager());
            for (RentalKeyHistoryData data : history) {
                if (getFloorFromRoomNumber(data.getRoomNumber()) == selectedFloor) {
                    tblRoomList.getItems().add(data);
                }
            }
        } catch (SQLException e) {
            getSceneManager().showErrorAlert(e.toString());
            e.printStackTrace();
        }


    }

    public void btnBack_OnAction() throws IOException {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }

    public void btnGroundFloor_OnAction(ActionEvent actionEvent) {
        fillRoomList(0);
    }

    public void btnFirstFloor_OnAction(ActionEvent actionEvent) {
        fillRoomList(1);
    }

    public void btnSecondFloor_OnAction(ActionEvent actionEvent) {
        fillRoomList(2);
    }

    public void btnThirdFloor_OnAction(ActionEvent actionEvent) {
        fillRoomList(3);
    }
}
