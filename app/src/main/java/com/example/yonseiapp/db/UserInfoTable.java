package com.example.yonseiapp.db;

import android.app.Activity;
import android.content.Context;

import com.example.yonseiapp.utils.Utils;

import org.json.JSONObject;

public class UserInfoTable extends JSONObject {

    private static UserInfoTable instance = new UserInfoTable();
    public static UserInfoTable inst() {return instance; }

    public void load(Context ctx){
        Utils.JsonLoad(UserInfoTable.class.getName(), this, ctx);
    }

    private JSONObject data = new JSONObject();

    public void put(JSONObject params, Activity act){
        try{
            JSONObject userinfo = params.getJSONObject("userinfo");
            System.out.println("put userinfo : " + userinfo);

            if (userinfo != null) {
                Utils.JsonMerge(this, userinfo);
                Utils.JsonSave("UserInfoTable", this, act);
            }

        }catch (Exception e){
            System.out.println("UserInfoTable put : " + e);
        }
    }

    public JSONObject get(){
        try{
            System.out.println("jsonobject get : " + this.toString());
            //return (JSONObject)this.get("userinfo");
            return (JSONObject)this;
        }catch(Exception e){
            System.out.println("UserInfo Table get : " + e);
        }
        return null;
    }

    public void del(Activity act){
        //Utils.JsonClear(this);
        Utils.JsonSave("UserInfoTable",this,act);
    }
}
