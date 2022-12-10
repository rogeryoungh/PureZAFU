package pers.roger.purezafu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import pers.roger.purezafu.R;
import pers.roger.purezafu.presenter.ILoginPresenter;
import pers.roger.purezafu.presenter.LoginPresenter;
import pers.roger.purezafu.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    EditText usernameEdit;
    EditText passwordEdit;
    Button loginButton;
    RelativeLayout activity_login;
    CheckBox autologinCheckBox;
    SharedPreferences userInfo;

    SharedPreferences.Editor editor;
    ILoginPresenter loginPresenter;

    boolean autologin = false;
    boolean firstload = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.button_login);
        activity_login = findViewById(R.id.activity_login);
        autologinCheckBox = findViewById(R.id.checkbox_autologin);
        loginPresenter = new LoginPresenter(this);

        userInfo = getSharedPreferences("user", MODE_PRIVATE);

        firstload = userInfo.getBoolean("firstload", true);
        if (firstload) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("课余作品，陆续会增加一些小功能，欢迎尝试 v0.1！")
                    .setPositiveButton("已阅", null)
                    .show();
        }
        autologin = userInfo.getBoolean("autologin", false);
        if (autologin) {
            autologinCheckBox.setChecked(true);
        }

        String username = userInfo.getString("username", "");
        String password = userInfo.getString("password", "");
        usernameEdit.setText(username);
        passwordEdit.setText(password);

        loginButton.setEnabled(false);
        loginPresenter.checkUpdate();
    }

    public SharedPreferences.Editor getUserInfoEditor() {
        return userInfo.edit();
    }

    public void autoLogin() {
        if (autologin) {
            String token = userInfo.getString("token", "");
            if (token.length() == 224) {
                startMain();
            } else {
                userLogin();
            }
        } else {
            loginButton.setEnabled(true);
            showSnackbar("无需更新");
        }
    }

    public void startMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onUserLoginSuccess() {
        editor = userInfo.edit();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void setLoginButton(boolean enable) {
        if (enable) {
            loginButton.setEnabled(true);
            loginButton.setText("登录");
        } else {
            loginButton.setEnabled(false);
            loginButton.setText("登录中...");
        }
    }

    public void userLogin() {
        editor = userInfo.edit();
        editor.putBoolean("autologin", autologinCheckBox.isChecked());
        editor.apply();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (username.length() > 0 && password.length() > 0) {
            setLoginButton(false);
            loginPresenter.userLogin(username, password);
        } else {
            showSnackbar("请填写完整");
        }
    }


    public void requireUpdate(String downloadUrl) {
        Snackbar.make(activity_login, "需要更新，请先使用原版，等待适配！继续使用请自行负责！", 10000)
                .setAction("原版", v -> {
                    Uri uri = Uri.parse(downloadUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }).show();
        setLoginButton(true);
    }

    public void showSnackbar(String tips) {
        Log.i("SNACKBAR", tips);
        Snackbar.make(activity_login, tips, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void login_click(View view) {
        userLogin();
    }
}