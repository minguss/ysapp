package com.example.yonseiapp.activities.myProfile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yonseiapp.api.RetCallBack;
import com.example.yonseiapp.api.UserInfo;
import com.example.yonseiapp.utils.Utils;
import com.example.yonseiapp.R;

public class MyProfileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        TextView topTitle = findViewById(R.id.topTitle);
        TextView topRight = findViewById(R.id.topRight);

        topTitle.setText("My Profile Edit");
        topRight.setText("Save");

        String name = getIntent().getStringExtra("name");
        String age = getIntent().getStringExtra("age");

        EditText etName = findViewById(R.id.name);
        EditText etAge = findViewById(R.id.age);
        etName.setText(name);
        etAge.setText(age);
    }

    public void onClickBack(View v){
        onBackPressed();
    }

    public void onClickMyProfileEdit(View v){
        Intent result = new Intent();

        EditText etName = findViewById(R.id.name);
        EditText etAge = findViewById(R.id.age);

        String name = etName.getText().toString();
        String age = etAge.getText().toString();

        result.putExtra("name", etName.getText().toString());
        result.putExtra("age", etAge.getText().toString());

        setResult(100, result);
        UserInfo.put(this, name, age, new RetCallBack() {
            @Override
            public void onResponse(Boolean ret, String errMsg) {
                if(ret == true)
                    finish();
                else
                    Utils.toast(MyProfileEditActivity.this, errMsg);
            }
        });
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        TextView tvName = findViewById(R.id.name);
        TextView tvAge = findViewById(R.id.age);

        if(requestCode == 1000 && resultCode == 100){
            String name = data.getStringExtra("name");
            String age = data.getStringExtra("age");

            tvName.setText(name);
            tvAge.setText(age);
        }
    }
}