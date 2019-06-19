package com.example.yonseiapp.utils;

import org.json.JSONObject;

public interface PostCallBack {
    void onResponse(JSONObject ret, String errMsg);
}