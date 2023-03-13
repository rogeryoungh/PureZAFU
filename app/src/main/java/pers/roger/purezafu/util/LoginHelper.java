package pers.roger.purezafu.util;


import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.ValueCallback;

import com.google.android.material.snackbar.Snackbar;

import pers.roger.purezafu.activity.WebActivity;

public class LoginHelper {
    private final WebActivity webActivity;
    String username;
    String password;

    public LoginHelper(WebActivity webActivity) {
        this.webActivity = webActivity;
        SharedPreferences userInfo = webActivity.getSharedPreferences("user", android.content.Context.MODE_PRIVATE);
        username = userInfo.getString("username", "");
        password = userInfo.getString("password", "");
    }

    public void eval(String js, ValueCallback<String> callback) {
        webActivity.webView.evaluateJavascript("javascript:" + js, callback);
    }

    public void eval(String js) {
        webActivity.webView.evaluateJavascript("javascript:" + js, v -> {
        });
    }

    public void autoFill() {
        Snackbar.make(webActivity.constraintLayout, "检测到需要登录", 5000).setAction("填充", v -> {
            eval("$('#password')[0].value = '" + password + "'");
            eval("$('#username')[0].value = '" + username + "'");
        }).show();
    }
}
