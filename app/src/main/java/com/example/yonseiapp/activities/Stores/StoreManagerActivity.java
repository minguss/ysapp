package com.example.yonseiapp.activities.Stores;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yonseiapp.R;
import com.example.yonseiapp.activities.MainActivity;
import com.example.yonseiapp.db.StoreTable;

public class StoreManagerActivity extends AppCompatActivity {
    StoreAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager);

        ((TextView)findViewById(R.id.topTitle)).setText("StoreManager");
//      findViewById(R.id.topRight).setVisibility(View.GONE);

        TextView topRight = ((TextView)findViewById(R.id.topRight));
        topRight.setText("Add Store");
        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StoreManagerActivity.this, StoreAddActivity.class);
                startActivity(i);
            }
        });


        if (StoreTable.inst().size() == 0)
            putStores();

//        ((RecyclerView)findViewById(R.id.recyclerView)).setAdapter(new StoreAdapter());


        adapter = new StoreAdapter(new StoreAdapter.DelCallBack() {
            @Override
            public void del(final int idx) {
                new AlertDialog.Builder(StoreManagerActivity.this)
                        .setMessage("지우시겠습니까?")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StoreTable.inst().del(idx);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        ((RecyclerView)findViewById(R.id.recyclerView)).setAdapter(adapter);
    }

    private void putStores() {
        String[] names = {"Starbucks", "Coffeebean", "Bluebottle", "Ediya", "Orot", "Zagmachi", "Nespresso", "Illy"};
        double []lats = {37.558997,37.558980,37.558665,37.558529,37.557908,37.557117,
                37.556990,37.556394,37.556794,37.555901};
        double []lngs = {126.936183,126.936741,126.93666,126.937127,126.937116,126.936376,
                126.934713,126.937191,126.937031,126.938511};
        Integer []coupons = {100,101,102,103,104,105,106,107};
        for (int i = 0; i < names.length; i++)
            StoreTable.inst().put(i, lats[i],lngs[i],names[i].concat("입니다."), names[i],coupons[i]);
    }

    public void onClickBack(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}