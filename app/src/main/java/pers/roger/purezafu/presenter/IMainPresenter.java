package pers.roger.purezafu.presenter;

public interface IMainPresenter extends IPresenter {
    int POST_APPDATA = 0;

    void getAppData(String token);
}
