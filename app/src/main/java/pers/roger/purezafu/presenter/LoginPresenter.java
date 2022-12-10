package pers.roger.purezafu.presenter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import pers.roger.purezafu.model.UserLoginModel;
import pers.roger.purezafu.model.PostModel;
import pers.roger.purezafu.model.UpgradeModel;
import pers.roger.purezafu.view.ILoginView;

public class LoginPresenter implements ILoginPresenter {
    UpgradeModel upgradeModel;
    UserLoginModel userLoginModel;
    ILoginView loginView;
    Handler handler;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        upgradeModel = new UpgradeModel();
        userLoginModel = new UserLoginModel();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case POST_UPGRADE:
                        Log.i("HANDLER", "POST_UPGRADE");
                        upgrade_back();
                        break;
                    case POST_USERLOGIN:
                        Log.i("HANDLER", "POST_USERLOGIN");
                        login_back();
                        break;
                }
            }
        };
    }

    public void upgrade_back() {
        if (upgradeModel.isSuccess()) {
            if (upgradeModel.getBean().getUpdateRequired()) {
                loginView.requireUpdate(upgradeModel.getBean().getDownloadUrl());
            } else {
                loginView.autoLogin();
            }
        } else {
            loginView.showSnackbar("检查更新失败，请检查网络");
        }
    }

    public void login_back() {
        loginView.setLoginButton(true);
        if (userLoginModel.isSuccess()) {
            if (userLoginModel.getBean().getType().equals("success")) {
                SharedPreferences.Editor editor = loginView.getUserInfoEditor();

                editor.putString("token", userLoginModel.getBean().getData().getToken());
                editor.putString("name", userLoginModel.getBean().getData().getName());
                editor.putBoolean("isTeacher", userLoginModel.getBean().getData().isTeacher());

                Gson gson = new Gson();
                String json = gson.toJson(userLoginModel.getBean());
                editor.putString("user_login", json);

                editor.apply();
                loginView.onUserLoginSuccess();
            } else {
                loginView.showSnackbar(userLoginModel.getBean().getContent());
            }
        } else {
            loginView.showSnackbar("登录失败，请检查网络");
        }
    }

    public void userLogin(String username, String password) {
        userLoginModel.login(new PostModel.Postback() {
            @Override
            public void postback() {
                handler.sendEmptyMessage(POST_USERLOGIN);
            }
        }, PostModel.POSTMODE_APP_HTTPS, username, password);
    }

    @Override
    public void checkUpdate() {
        upgradeModel.checkUpdate(new PostModel.Postback() {
            @Override
            public void postback() {
                handler.sendEmptyMessage(POST_UPGRADE);
            }
        }, PostModel.POSTMODE_APP_HTTPS);
    }
}
