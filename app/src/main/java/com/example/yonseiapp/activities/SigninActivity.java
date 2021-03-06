package com.example.yonseiapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yonseiapp.R;
import com.example.yonseiapp.db.SessionTable;
import com.example.yonseiapp.utils.PostCallBack;
import com.example.yonseiapp.utils.Utils;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class SigninActivity extends AppCompatActivity {

    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SigninActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void onClickSignIn(View v) {
        EditText etID = findViewById(R.id.suetID);
        EditText etPwd = findViewById(R.id.suetPwd);

        String id = etID.getText().toString();
        String pwd = etPwd.getText().toString();

        try {
            JSONObject json = new JSONObject();
            json.put("command", "signin");
            json.put("ID", id);
            json.put("pwd", pwd);

            Utils.post(json, new Utils.PostCallBack() {
                @Override
                public void onResponse(JSONObject ret, String errMsg) {
                    try {
                        if (errMsg != null) {
                            Utils.toast(SigninActivity.this, errMsg);
                            return;
                        }

                        if (ret.getBoolean("ret") == false) {
                            Utils.toast(SigninActivity.this, ret.getString("message"));
                            return;
                        }

                        Utils.toast(SigninActivity.this, "로그인 성공");
                        System.out.println("debug 1");

                        String utk = ret.getString("sessionID");

                        System.out.println("debug 2");

                        SessionTable.inst().putSession(SigninActivity.this, utk);

                        System.out.println("debug 3");

                        //로그인이 잘 끝났으니 MainActivity로 화면을 바꿔주고 종료
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        System.out.println("SignIn Exception : " + e);
                    }
                }
            });
        } catch (Exception e ){
            System.out.println("SignIn Exception2 : " + e);
        }
    }

    public void onClickSignUp(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
