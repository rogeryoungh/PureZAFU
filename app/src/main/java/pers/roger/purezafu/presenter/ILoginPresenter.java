package pers.roger.purezafu.presenter;

public interface ILoginPresenter extends IPresenter {
    int POST_UPGRADE = 0;
    int POST_USERLOGIN = 1;

    void checkUpdate();
    void userLogin(String username, String password);
}
