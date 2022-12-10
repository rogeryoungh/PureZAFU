package pers.roger.purezafu.view;

import android.content.SharedPreferences;

public interface ILoginView {
    void requireUpdate(String downloadUrl);
    void showSnackbar(String tips);
    void setLoginButton(boolean enable);
    void onUserLoginSuccess();
    SharedPreferences.Editor getUserInfoEditor();
    void autoLogin();
}
