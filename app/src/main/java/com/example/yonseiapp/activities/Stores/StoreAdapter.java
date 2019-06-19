package com.example.yonseiapp.activities.Stores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yonseiapp.R;
import com.example.yonseiapp.db.StoreTable;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.yonseiapp.db.StoreTable.inst;

public class StoreAdapter extends RecyclerView.Adapter {


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            del = itemView.findViewById(R.id.del);
        }
    }

    //지우기 방법 2 - 메인 Activity에서 함수처리
    interface DelCallBack{
        void del(int idx);}
        DelCallBack delcallBack = null;

    //지우기 버튼을 누르면 이 콜백을 실행시킴킴
   public StoreAdapter(DelCallBack cb){
        delcallBack = cb;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_store, viewGroup, false);

        RecyclerView.ViewHolder viewHolder = new ViewHolder(view);

        //ViewHolder return
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        JSONObject item = StoreTable.inst().get(i);
        try{
            ViewHolder v = (ViewHolder)viewHolder;
            v.name.setText(item.getString("name"));
            v.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    StoreTable.inst().del(i);
//                    notifyDataSetChanged();
                    delcallBack.del(i);
                    notifyDataSetChanged();
                }
            });
            v.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delcallBack.del(i);
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return StoreTable.inst().size();
    }

}