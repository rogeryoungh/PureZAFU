package pers.roger.purezafu.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pers.roger.purezafu.model.bean.UpgradeBean;
import pers.roger.purezafu.util.Utils;

public class UpgradeModel extends PostModel {
    private final OkHttpClient client;
    String posturl = "/upgrade/upgrade.jhtm";
    UpgradeBean bean;

    public UpgradeModel() {
        client = new OkHttpClient();
        bean = new UpgradeBean();
    }

    public void checkUpdate(Postback postback, int mode) {
        String url = getURL(posturl, mode);
        RequestBody formBody = new FormBody.Builder()
                .add("channel", "android")
                .add("appVersion", Utils.version_string)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Log.i("POST", url);
        client.newCall(request).enqueue(new PostCallback() {
            @Override
            public void response(String responseData) throws JsonSyntaxException {
                setSuccess(true);
                Gson gson = new Gson();
                bean = gson.fromJson(responseData, UpgradeBean.class);
                postback.postback();
            }

            @Override
            public void failure() {
                setSuccess(false);
                postback.postback();
            }
        });
    }

    public UpgradeBean getBean() {
        return bean;
    }
}
