package pers.roger.purezafu.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pers.roger.purezafu.model.bean.AppDataBean;

public class AppDataModel extends PostModel {
    private final OkHttpClient client;
    String posturl = "/app/data.jhtm";
    AppDataBean bean;

    public AppDataModel() {
        client = new OkHttpClient();
    }


    public void getAppData(PostModel.Postback postback, int mode, String token) {
        String url = getURL(posturl, mode);
        RequestBody formBody = new FormBody.Builder()
                .add("token", token)
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
                bean = gson.fromJson(responseData, AppDataBean.class);
                Collections.sort(bean.getData().getCatApps(), (o1, o2) -> Integer.compare(o1.getCatId(), o2.getCatId()));
                postback.postback();
            }

            @Override
            public void failure() {
                postback.postback();
            }
        });
    }

    public AppDataBean getBean() {
        return bean;
    }
}
