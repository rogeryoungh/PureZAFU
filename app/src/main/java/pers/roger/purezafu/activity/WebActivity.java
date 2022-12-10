package pers.roger.purezafu.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Method;

import pers.roger.purezafu.R;
import pers.roger.purezafu.util.LocationHelper;

public class WebActivity extends AppCompatActivity {

    public WebView webView;
    public ConstraintLayout constraintLayout;
    public String nowUrl = "";

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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false); // 页面有请求位置的时候需要
            }
        });

        webView.addJavascriptInterface(new LocationHelper(this), "zafu");


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                switch (error.getPrimaryError()) {
                    case SslError.SSL_INVALID: // 校验过程遇到了bug
                    case SslError.SSL_UNTRUSTED: // 证书有问题
                        handler.proceed();
                    default:
                        handler.cancel();
                }
            }
        });
        nowUrl = intent.getStringExtra("url");
        webView.loadUrl(nowUrl);
    }
}