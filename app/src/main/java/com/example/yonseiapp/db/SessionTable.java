package com.example.yonseiapp.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;
import com.example.yonseiapp.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class SessionTable extends JSONObject {
    //Singleton
    private static SessionTable instance = new SessionTable();
    public static SessionTable inst() {return instance;}


    public void load(Activity act){
        SharedPreferences sharedPreferences = act.getSharedPreferences("ysAPP", MODE_PRIVATE);
        String json = sharedPreferences.getString("SessionTable", null);
        if(json == null)
            return;
        try{
            JSONObject load = new JSONObject(json);
            put("utk", load.getString("utk"));
        }catch (Exception e ){
            System.out.println("load : " + e);
        }
    }

    public void putSession(Activity act, String utk){
        try {
            put("utk", utk);

            SharedPreferences sharedPreferences = act.getSharedPreferences("ysAPP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SessionTable", this.toString());
            editor.commit();
        } catch (Exception e) {
            System.out.println("putSession : " + e);
        }

    }

    public void pullSession(Activity act){
        try {
            remove("utk");

            SharedPreferences sharedPreferences = act.getSharedPreferences("ysAPP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SessionTable", this.toString());
            editor.commit();

        } catch(Exception e) {
            System.out.println("pullSession : " + e);
        }
    }
    public String getSession() {
        try{
            String utk = getString("utk");
            return utk;
        }catch (Exception e) {
            System.out.println("getSession : " + e);
        }
        return null;
    }

    public void delSession(Activity act){
        remove("utk");
        SharedPreferences sharedPreferences = act.getSharedPreferences("ysAPP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("SessionTable");
        editor.apply();
    }

    public void load(Context ctx){
        Utils.JsonLoad(UserInfoTable.class.getName(), this, ctx);
    }
}
