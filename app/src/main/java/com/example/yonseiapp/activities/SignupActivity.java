package com.example.yonseiapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.yonseiapp.R;
import com.example.yonseiapp.utils.PostCallBack;
import com.example.yonseiapp.utils.Utils;

import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    public void onClickSignup(View v) {
        EditText dtID = findViewById(R.id.suetID);
        EditText dtPW = findViewById(R.id.suetPwd);
        String id = dtID.getText().toString();
        String pwd = dtPW.getText().toString();

        try {
            JSONObject json = new JSONObject();
            json.put("ID", id);
            json.put("command", "signup");
            json.put("pwd", pwd);

            Utils.post(json, new Utils.PostCallBack() {
                @Override
                public void onResponse(JSONObject ret, String errMsg) {
                    try {
                        if (errMsg !=null) {
                            Utils.toast(SignupActivity.this, errMsg);
                            return ;
                        }
                        if (ret.getBoolean("ret") == false) {
                            Utils.toast(SignupActivity.this, ret.getString("message"));
                            return;
                        }
                        Utils.toast(SignupActivity.this, "가입성공! 로그인해주세요");
                        finish();
                    } catch (Exception e) {
                        System.out.println("onClickSignup onResponse : " + e);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("onClickSignup : " + e);
        }
    }

}
