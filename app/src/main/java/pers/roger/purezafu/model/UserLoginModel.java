package pers.roger.purezafu.model;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pers.roger.purezafu.model.bean.UserLoginBean;
import pers.roger.purezafu.util.AESUtils;
import pers.roger.purezafu.util.Utils;

public class UserLoginModel extends PostModel {
    private final OkHttpClient client;
    String posturl = "/user/login.jhtm";
    UserLoginBean bean;

    public UserLoginModel() {
        client = new OkHttpClient();
        bean = new UserLoginBean();
    }

    public void login(PostModel.Postback postback, int mode, String username, String password) {
        String url = getURL(posturl, mode);
        RequestBody formBody = new FormBody.Builder()
                .add("userName", username)
                .add("pswd", AESUtils.encryptAppPassword(password, "zafuhzlgzafuhzlg", "zafu_hzlg_123456"))
                .add("appVersion", Utils.version_string)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Log.i("POST", url);
        client.newCall(request).enqueue(new PostCallback() {
            @Override
            public void response(String responseData) {
                Gson gson = new Gson();
                bean = gson.fromJson(responseData, UserLoginBean.class);
                postback.postback();
            }

            @Override
            public void failure() {
                postback.postback();
            }
        });
    }

    public UserLoginBean getBean() {
        return bean;
    }
}
