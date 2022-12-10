package pers.roger.purezafu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import pers.roger.purezafu.R;
import pers.roger.purezafu.adapter.AppsAdapter;
import pers.roger.purezafu.model.bean.AppDataBean;
import pers.roger.purezafu.presenter.IMainPresenter;
import pers.roger.purezafu.presenter.MainPresenter;
import pers.roger.purezafu.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {
    RelativeLayout activity_main;
    RecyclerView list_apps;

    IMainPresenter mainPresenter;

    SharedPreferences userInfo;
    SharedPreferences appInfo;
    SharedPreferences.Editor editor;

    AppsAdapter appsAdapter;

    String token;
    boolean hasload = false;
    AppDataBean appDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.activity_main);
        list_apps = findViewById(R.id.list_apps);

        userInfo = getSharedPreferences("user",MODE_PRIVATE);
        appInfo = getSharedPreferences("app",MODE_PRIVATE);

        String name = userInfo.getString("name",null);
        boolean isTeacher = userInfo.getBoolean("isTeacher",false);
        if(isTeacher) {
            showSnackbar("欢迎回来 "+name+" 老师");
        } else {
            showSnackbar("欢迎回来 "+name+" 同学");
        }

        token = userInfo.getString("token",null);
        hasload = appInfo.getBoolean("hasload",false);

        mainPresenter = new MainPresenter(this);

        boolean firstload = userInfo.getBoolean("firstload",true);
        if(firstload) {
            editor = userInfo.edit();
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("登录问题还未解决，第一次需要手动登录。\n如遇到“无法登录”请尝试点入其他页面再进入！")
                    .setPositiveButton("已阅",null)
                    .show();
            editor.putBoolean("firstload",false);
            editor.apply();
        }

        list_apps.setLayoutManager(new LinearLayoutManager(this));
        list_apps.setItemAnimator(new DefaultItemAnimator());
        appsAdapter = new AppsAdapter(this);

        if(hasload) {
            String json = appInfo.getString("app_data","");
            Log.i("appdata",json);
            Gson gson = new Gson();
            appDataBean = gson.fromJson(json,AppDataBean.class);
            appsAdapter.setAppData(appDataBean);
            list_apps.setAdapter(appsAdapter);
        }
        mainPresenter.getAppData(token);
    }

    public void getAppData_back(AppDataBean appData) {
        if(hasload)
            return;
        appsAdapter.setAppData(appData);
        list_apps.setAdapter(appsAdapter);
    }

    @Override
    public SharedPreferences.Editor getAppInfoEditor() {
        return appInfo.edit();
    }

    public void showSnackbar(String tips) {
        Log.i("SNACKBAR",tips);
        Snackbar.make(activity_main,tips,Snackbar.LENGTH_SHORT)
                .show();
    }
}