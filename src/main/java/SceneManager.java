import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.EnumMap;

public class SceneManager {

    public enum SceneType {
        Login,
        Menu,
        RentKey,
        ReturnKey,
        AddKey,
        AddEmployee,
        RentalKeyHistory,
        KeyHistory
    }

    private Stage mainStage = null;
    private EnumMap<SceneType, Parent> scenes = new EnumMap<>(SceneType.class);
    private EnumMap<SceneType, BaseController> controllers = new EnumMap<>(SceneType.class);
    private SceneType currentlyShown;

    public void loadScene(SceneType sceneType, String url) {
        try {
            FXMLLoader sceneLoader = new FXMLLoader();
            sceneLoader.setLocation(getClass().getResource(url));
            Parent sceneRoot = sceneLoader.load();
            BaseController controller = sceneLoader.getController();
            storeController(sceneType, controller);
            scenes.put(sceneType, sceneRoot);
        } catch (Exception ex) {
            showErrorAlert(ex.toString());
            ex.printStackTrace();
        }
    }

    public void storeController(SceneType sceneType, BaseController controller) {
        controller.setSceneManager(this);
        controller.OnLoad();
        controllers.put(sceneType, controller);
    }

    public Parent getSceneRoot(SceneType sceneType) {
        return scenes.get(sceneType);
    }

    public void showScene(SceneType sceneType, boolean clean) {
        if (currentlyShown != null) controllers.get(currentlyShown).OnClose();
        Parent sceneRoot = scenes.get(sceneType);
        if (sceneRoot != null) {
            getMainStage().getScene().setRoot(sceneRoot);
        }
        controllers.get(sceneType).OnShow(clean);
        currentlyShown = sceneType;
    }

    public void showSceneWithIntValue(SceneType sceneType, boolean clean, int value) {
        if (currentlyShown != null) controllers.get(currentlyShown).OnClose();
        Parent sceneRoot = scenes.get(sceneType);
        if (sceneRoot != null) {
            getMainStage().getScene().setRoot(sceneRoot);
        }
        controllers.get(sceneType).OnShowWithInt(clean, value);
        currentlyShown = sceneType;
    }

    public void stageClosed() {
        if (currentlyShown != null) controllers.get(currentlyShown).OnClose();
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initOwner(getMainStage());
        alert.showAndWait();
    }

    public void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.initOwner(getMainStage());
        alert.showAndWait();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
