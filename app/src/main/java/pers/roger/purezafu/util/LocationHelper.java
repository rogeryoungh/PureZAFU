package pers.roger.purezafu.util;

import android.Manifest;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;

import com.google.android.material.snackbar.Snackbar;

import pers.roger.purezafu.activity.WebActivity;

public class LocationHelper {
    private final WebActivity webActivity;

    public LocationHelper(WebActivity webActivity) {
        this.webActivity = webActivity;
    }

    public void eval(String js, ValueCallback<String> callback) {
        webActivity.webView.evaluateJavascript("javascript:" + js, callback);
    }

    public void eval(String js) {
        webActivity.webView.evaluateJavascript("javascript:" + js, v -> {
        });
    }

    @JavascriptInterface
    public void gdGetLocation() {
        String[] manifests = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webActivity.requestPermissions(manifests, 100);
        }

        webActivity.runOnUiThread(() -> eval("window.location.href", nowUrl -> {
            if (nowUrl.contains("h5app/checkinclass.htm")) {
                check();
            } else {
                Snackbar.make(webActivity.constraintLayout, "未知定位使用", 5000).show();
            }
        }));

    }

    private void check() {
        eval("$('.lat').text()", lat -> eval("$('.lng').text()", lng -> Snackbar
                .make(webActivity.constraintLayout, "上课啦签到：北纬" + lat + "东经：" + lng, 5000)
                .setAction("继续", v -> {
                    String callback = "(s, r) => zafu.sendLocation(r.locations[0].lat, r.locations[0].lng)";
                    String call = "AMap.convertFrom([" + lng + ", " + lat + "], 'gps', " + callback + ")";
                    Log.i("zafu", call);
                    eval(call);
                }).show()));
    }

    @JavascriptInterface
    public void sendLocation(String lat, String lng) {
        String params = "\"success\", " + lat + ", " + lng;
        Log.i("zafu", params);
        webActivity.runOnUiThread(() -> eval("gdGetLocationCallback(" + params + ")"));
    }
}
