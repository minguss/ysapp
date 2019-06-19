package com.example.yonseiapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yonseiapp.R;
import com.example.yonseiapp.activities.Stores.StoreManagerActivity;
import com.example.yonseiapp.activities.myProfile.MyProfileViewActivity;
import com.example.yonseiapp.db.SessionTable;
import com.example.yonseiapp.db.StoreTable;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    GoogleMap gMap = null;
    // onRequestPermissionsResult에서 구별자
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    //필요한 퍼미션들
    String[] REQUIRED_PERMISSIONS  = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grandResults) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE) {
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
            //퍼미션을 허용했다면 위치를 받아오자

            if (gMap !=null){
                if (checkPermission())
                    gMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 내 위치를 받아옵시다.
        }else {
            //퍼미션이 없으면 계속요청
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS ,PERMISSIONS_REQUEST_CODE);
        }

                //이동

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //지도에 추가 작업을 할 수 있을 때 호출 되도록 세팅
                MarkerOptions marker = marker(37.558895, 126.936923, "신촌", "신촌입니다");
                googleMap.addMarker(marker);
                LatLng pos = marker.getPosition();
                CameraUpdate cam = CameraUpdateFactory.newLatLng(pos);
                googleMap.moveCamera(cam);

                CameraUpdate cam2 = CameraUpdateFactory.zoomTo(17);
                googleMap.animateCamera(cam2);

                if (StoreTable.inst().size() == 0){
                    String[] names = {"Starbucks", "Coffeebean", "Bluebottle", "Ediya", "Orot", "Zagmachi", "Nespresso", "Illy"};
                    double []lats = {37.558997,37.558980,37.558665,37.558529,37.557908,37.557117,
                            37.556990,37.556394,37.556794,37.555901};
                    double []lngs = {126.936183,126.936741,126.93666,126.937127,126.937116,126.936376,
                            126.934713,126.937191,126.937031,126.938511};
                    Integer []coupons = {100,101,102,103,104,105,106,107};
                    for (int i = 0; i < names.length; i++)
                        StoreTable.inst().put(i, lats[i],lngs[i],names[i].concat("입니다."), names[i],coupons[i]);
                }
                addAllMarker();
                int markernum = StoreTable.inst().size();
                JSONObject store = null;

                for (int i = 0; i<markernum; i++){
                    store = StoreTable.inst().get(i);
                    Log.d("store", "store"+store);

                    try{
                        marker = marker(store.getDouble("lat"), store.getDouble("lng"), store.getString("desc"), store.getString("name") );
                        googleMap.addMarker(marker);
//                                .setTag(store.getInt("coupon"));;


                    }catch(Exception e) {}

                }
                gMap = googleMap;
                if(checkPermission())
                    googleMap.setMyLocationEnabled(true);
            }
        });
    }


    public void addNewMarker(int i){
        JSONObject store = StoreTable.inst().get(i);
        try {
            MarkerOptions marker = marker(store.getDouble("lat"), store.getDouble("lng"), store.getString("desc"), store.getString("name"));
            gMap.addMarker(marker);
        }catch(Exception e) {}

    }

    public void addAllMarker(){
        int markernum = StoreTable.inst().size();
        JSONObject store = null;
        for (int i = 0; i<markernum; i++){
            store = StoreTable.inst().get(i);
             try{
                MarkerOptions marker = marker(store.getDouble("lat"), store.getDouble("lng"), store.getString("desc"), store.getString("name") );
                gMap.addMarker(marker);
//                marker.setTag(store.getInt("coupon"));;

            }catch(Exception e) {}

        }
    }


    public boolean onMarkerClick(final Marker marker) {
        //지도를 원하는 위치로 이동
        LatLng pos = marker.getPosition();
        CameraUpdate cam = CameraUpdateFactory.newLatLng(pos);

        gMap.animateCamera(cam);

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount - 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " 쿠폰이 " + clickCount + " 개 남았습니다.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private boolean checkPermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED);

    }

    public void onClickLogout(View v) {
        SessionTable.inst().pullSession(MainActivity.this);
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickBack(View v) {
        finish();
    }
    public void onClickStoreManager(View v){
        Intent intent = new Intent(this, StoreManagerActivity.class);
        startActivity(intent);
        //추가
        finish();


    }
    public void onClickMyProfile(View v) {
        Intent intent = new Intent(this, MyProfileViewActivity.class);
        intent.putExtra("test", "test");
        intent.putExtra("testInt", 1);

        startActivity(intent);
        addAllMarker();



    }

    private MarkerOptions marker(double lat, double lng, String name, String desc) {
        LatLng seoul = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(seoul);
        markerOptions.title(name);
        markerOptions.snippet(desc);
        return markerOptions;

    }




}