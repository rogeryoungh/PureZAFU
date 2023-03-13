package pers.roger.purezafu.presenter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import pers.roger.purezafu.model.AppDataModel;
import pers.roger.purezafu.model.PostModel;
import pers.roger.purezafu.view.IMainView;

public class MainPresenter implements IMainPresenter {
    IMainView mainView;
    AppDataModel appDataModel;
    Handler handler;

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;
        appDataModel = new AppDataModel();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                super.dispatchMessage(msg);
                if (msg.what == POST_APPDATA) {
                    Log.i("HANDLER", "POST_APPDATA");
                    appdata_back();
                }
            }
        };
    }

    private void appdata_back() {
        Log.i("APPDATA", "GET APPDATA SUCCESS!");
        SharedPreferences.Editor editor = mainView.getAppInfoEditor();
        Gson gson = new Gson();
        String json = gson.toJson(appDataModel.getBean());
        Log.i("APPDATA", json);
        editor.putString("app_data", json);
        editor.putBoolean("hasload", true);
        editor.apply();

        mainView.getAppData_back(appDataModel.getBean());
    }

    @Override
    public void getAppData(String token) {
        appDataModel.getAppData(new PostModel.Postback() {
            @Override
            public void postback() {
                handler.sendEmptyMessage(POST_APPDATA);
            }
        }, PostModel.POSTMODE_APP_HTTPS, token);
    }
}
