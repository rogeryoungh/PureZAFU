package pers.roger.purezafu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Method;

import pers.roger.purezafu.R;
import pers.roger.purezafu.util.LocationHelper;
import pers.roger.purezafu.util.LoginHelper;

public class WebActivity extends AppCompatActivity {

    public WebView webView;
    public ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webview);
        constraintLayout = findViewById(R.id.webview_layout);

        Intent intent = getIntent();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 启用javascript
        settings.setDomStorageEnabled(true); // 支持HTML5中的一些控件标签
        settings.setBuiltInZoomControls(false); // 自选，非必要
        //处理http和https混合的问题
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        // 允许javascript出错
        try {
            Method method = Class.forName("android.webkit.WebView").
                    getMethod("setWebContentsDebuggingEnabled", Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(null, true);
        } catch (Exception e) {
            // do nothing
        }

        webView.setFocusable(true); // 自选，非必要
        webView.setDrawingCacheEnabled(true); // 自选，非必要
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 自选，非必要

        webView.addJavascriptInterface(new LocationHelper(this), "zafu");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false); // 页面有请求位置的时候需要
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(intent.getStringExtra("url"));

        new Handler().postDelayed(this::loadHelper, 1000);
    }

    public void eval(String js, ValueCallback<String> callback) {
        webView.evaluateJavascript("javascript:" + js, callback);
    }

    void loadHelper() {
        eval("window.location.href", nowUrl -> {
            Log.i("URL", nowUrl);
            if (nowUrl.contains("uis.zafu.edu.cn/cas/login")) {
                new LoginHelper(this).autoFill();
            } else if (nowUrl.contains("h5app/checkinclass.htm")) {
                Snackbar.make(constraintLayout, "已激活:强制签到", 5000).show();
            } else {
                Snackbar.make(constraintLayout, "null", 5000).show();
            }
        });
    }
}