package com.example.yonseiapp.activities.Stores;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yonseiapp.R;
import com.example.yonseiapp.api.RetCallBack;
import com.example.yonseiapp.api.UserInfo;
import com.example.yonseiapp.db.StoreTable;
import com.example.yonseiapp.db.UserInfoTable;
import com.example.yonseiapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Callback;

//import com.example.app.utils.Callback;

public class StoreAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add);

        updateToView();

        UserInfo.get(this, new RetCallBack() {
            @Override
            public void onResponse(Boolean ret, String errMsg) {
                if (ret)
                    updateToView();
            }
        });

        //intent 전달된 값을 꺼내옴
        String test = getIntent().getStringExtra("test");
        int testInt = getIntent().getIntExtra("testInt", 0);

        Utils.toast(this, test + "/" + testInt);
    }

    Callback cb;

    private void updateToView() {
        try {
            JSONObject val = UserInfoTable.inst().get();
            System.out.println("updateToView Val : " + val.toString());

            if (val != null) {
                String storename = val.getString("storename");
                String coupon = val.getString("coupon");
                String addr = val.getString("addr");
                String lat = val.getString("lat");
                String lng = val.getString("lng");


                TextView stName = findViewById(R.id.storename);
                TextView stCoupon = findViewById(R.id.coupon);
                TextView Address = findViewById(R.id.addr);
                TextView stLat = findViewById(R.id.lat);
                TextView stLng = findViewById(R.id.lng);
            }

        } catch (Exception e) {
            System.out.println("updateToView : " + e);
        }
    }

    public void onClickBack(View v) {
        onBackPressed();
    }

    public void onClickMyProfileEdit(View v) {
        String name = ((EditText) findViewById(R.id.storename)).getText().toString();
        double lat = Double.parseDouble(((TextView) findViewById(R.id.lat)).getText().toString());
        double lng = Double.parseDouble(((TextView) findViewById(R.id.lng)).getText().toString());
        Integer coupon = Integer.valueOf(((EditText) findViewById(R.id.coupon)).getText().toString());
        StoreTable.inst().put(StoreTable.inst().size() + 1, lat, lng, "새로등록된".concat(name).concat("입니다."), name,coupon);


        finish();
    }

    public void onClickGPS(View v) {
        //Gps 찾기 버튼 눌렀을 경우
        JSONObject params = new JSONObject();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?";
        String address = ((EditText) findViewById(R.id.addr)).getText().toString();
        System.out.println("click of add to gps");
        try {
            //Utils.post(url+"address="+address+"&key="+apikey, params.toString(), new Utils.PostCallBack() {
            Utils.callGPS(address, new Utils.PostCallBack() {
                public void onResponse(JSONObject ret, String errMsg) {
                    try {
                        System.out.println("result of get results :");
                        JSONArray arr = (JSONArray) ret.get("results");
                        JSONObject location = (new JSONObject(arr.get(0).toString())).getJSONObject("geometry").getJSONObject("location");
                        final Double lng = location.getDouble("lng");
                        final Double lat = location.getDouble("lat");

                        StoreAddActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.lat)).setText(lat.toString());
                                ((TextView) findViewById(R.id.lng)).setText(lng.toString());
                            }
                        });


                    } catch (Exception e) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
