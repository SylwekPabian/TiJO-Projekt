public class TestController extends BaseController {
    private boolean onLoadCalled = false;
    private boolean onShowCalled = false;

    @Override
    public void OnLoad() {
        super.OnLoad();
        onLoadCalled = true;
    }

    @Override
    public void OnShow(boolean clean) {
        super.OnShow(clean);
        onShowCalled = true;
    }

    public boolean isOnLoadCalled() {
        return onLoadCalled;
    }

    public boolean isOnShowCalled() {
        return onShowCalled;
    }
}
