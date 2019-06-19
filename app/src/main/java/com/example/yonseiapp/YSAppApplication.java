package com.example.yonseiapp;

import android.app.Application;
import android.util.Log;

import com.example.yonseiapp.db.SessionTable;
import com.example.yonseiapp.db.UserInfoTable;

public class YSAppApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        SessionTable.inst().load(this);
        UserInfoTable.inst().load(this);
        Log.e("application", "앱이 실행되고 SessionTable의 값이 불렸습니다.");
        Log.e("application", SessionTable.inst().toString());

    }
}
