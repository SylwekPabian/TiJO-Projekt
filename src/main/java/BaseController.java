public class BaseController {
    private SceneManager sceneManager;
    private DatabaseHelperInterface databaseHelper = DatabaseHelper.insatnce;

    public void OnLoad() {}
    public void OnShow(boolean clean) {}
    public void OnShowWithInt(boolean clean, int value) {}
    public void OnClose() {}

    public void setSceneManager(SceneManager manager) {
        sceneManager = manager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }
    public void setDatabaseHelper(DatabaseHelperInterface helper) { databaseHelper = helper; }
    public DatabaseHelperInterface getDatabaseHelper() { return databaseHelper; }
}
