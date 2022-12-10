package pers.roger.purezafu.view;

import android.content.SharedPreferences;

import java.util.List;

import pers.roger.purezafu.model.bean.AppDataBean;

public interface IMainView extends IView {

    void getAppData_back(AppDataBean appData);

    SharedPreferences.Editor getAppInfoEditor();
}
