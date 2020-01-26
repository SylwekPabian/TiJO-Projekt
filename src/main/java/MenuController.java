public class MenuController extends BaseController {
    public void btnRentKey_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.RentKey, true);
    }

    public void btnReturnKey_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.ReturnKey, true);
    }

    public void btnAddKey_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.AddKey, true);
    }

    public void addEmplyeeButtonAction() {
        getSceneManager().showScene(SceneManager.SceneType.AddEmployee, true);
    }

    public void rentalKeyHistoryButtonAction() {
        getSceneManager().showScene(SceneManager.SceneType.RentalKeyHistory, true);
    }

    public void btnLogout_OnAction() {
        getSceneManager().showScene(SceneManager.SceneType.Login, true);
    }
}
