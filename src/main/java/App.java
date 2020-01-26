import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    SceneManager sceneManager = new SceneManager();

    @Override
    public void start(Stage stage) throws Exception {
        GlobalData.instance.loadUsers();
        GlobalData.instance.loadKeys();

        sceneManager.setMainStage(stage);
        stage.setOnHiding(event -> {
            sceneManager.stageClosed();
            RxTxManager.instance.close();
        });

        stage.setTitle("Praca Dyplomowa");

        sceneManager.loadScene(SceneManager.SceneType.Login, "View/Login.fxml");
        sceneManager.loadScene(SceneManager.SceneType.Menu, "View/Menu.fxml");
        sceneManager.loadScene(SceneManager.SceneType.RentKey, "View/RentKey.fxml");
        sceneManager.loadScene(SceneManager.SceneType.ReturnKey, "View/ReturnKey.fxml");
        sceneManager.loadScene(SceneManager.SceneType.AddKey, "View/AddKey.fxml");
        sceneManager.loadScene(SceneManager.SceneType.AddEmployee, "View/AddEmployee.fxml");
        sceneManager.loadScene(SceneManager.SceneType.RentalKeyHistory, "View/RentalKeyHistory.fxml");
        sceneManager.loadScene(SceneManager.SceneType.KeyHistory, "View/KeyHistory.fxml");

        stage.setScene(new Scene(sceneManager.getSceneRoot(SceneManager.SceneType.Login), 1850.0, 1000.0));
        //stage.setFullScreen(true);
        stage.show();
        sceneManager.showScene(SceneManager.SceneType.Login, true);

        /*Alert alert = new Alert(Alert.AlertType.ERROR, "Test alertu, który nie minimalizuje fullscreenu!");
        alert.initOwner(stage);
        alert.showAndWait();*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}
