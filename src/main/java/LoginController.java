import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController extends BaseController {

    public Button loginButton;
    public Label lblFailedLogin;

    private class RfidReaderCommand implements RxTxManager.Command {
        @Override
        public void execute(String data) {
            processScannedId(data);
        }
    }

    @Override
    public void OnShow(boolean clean) {
        super.OnShow(clean);
        try {
            RxTxManager.instance.enableRfidReader(new RfidReaderCommand());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wystapił problem z czytnikiem RFID");
            alert.initOwner(getSceneManager().getMainStage());
            alert.showAndWait();
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
            try {
                UserData userData = new UserData(DatabaseHelper.insatnce);
                if (userData.isPorter(user.getIdUser())) {
                    getSceneManager().showScene(SceneManager.SceneType.Menu, true);
                } else {
                    lblFailedLogin.setVisible(true);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask()
                    {
                        public void run()
                        {
                            lblFailedLogin.setVisible(false);
                        }

                    };
                    timer.schedule(task, 3000l);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println("Nieznany identyfikator");
        //getSceneManager().showInfoAlert("Nieznany identyfikator!!");

    }

    public void btnClose_OnAction() {
        Platform.exit();
    }

    public void btnLogin_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.Menu, true);
    }
}
