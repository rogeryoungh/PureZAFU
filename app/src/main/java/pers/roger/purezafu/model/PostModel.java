package pers.roger.purezafu.model;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class PostModel {
    public static final int POSTMODE_APP_HTTP = 1;
    public static final int POSTMODE_APP_HTTPS = 2;
    public static final String appHost = "app.zafu.edu.cn/app";
    boolean success = false;

    public abstract static class Postback {
        protected abstract void postback();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    String getURL(String posturl, int mode) {
        String url = posturl;
        if (mode == POSTMODE_APP_HTTP)
            url = "http://" + appHost + url;
        else if (mode == POSTMODE_APP_HTTPS)
            url = "https://" + appHost + url;
        return url;
    }

    public boolean isSuccess() {
        return success;
    }

    public abstract class PostCallback implements Callback {
        /**
         * Prepares the [responseData] to be executed at some point in the future.
         */
        public abstract void response(String responseData) throws JsonSyntaxException;

        public abstract void failure();

        @Override
        public void onFailure(Call call, IOException e) {
            setSuccess(false);
            if (e instanceof SSLHandshakeException) {
                Log.i("POST", "证书错误。。抓包请切到 http");
            } else {
                failure();
                e.printStackTrace();
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();
            Log.i("POST", responseData);
            try {
                setSuccess(true);
                response(responseData);
            } catch (JsonSyntaxException e) {
                setSuccess(false);
                failure();
                e.printStackTrace();
            }
        }

    }
}
