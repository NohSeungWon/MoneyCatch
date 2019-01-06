package com.example.usser.moneycatch;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adapter_cal extends RecyclerView.Adapter<Adapter_cal.MyviewHoler> {
    String get = "로그용";

    static ArrayList<Calnederviewrecycle> items;
    Adapter_cal(ArrayList<Calnederviewrecycle> items){
        this.items = items;
    }
    final static  Adapter_cal adater_call = new Adapter_cal(items);

    public class MyviewHoler extends RecyclerView.ViewHolder {
        TextView calview;

        public MyviewHoler(View view) {
            super(view);
            calview = view.findViewById(R.id.calview);
            calview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final Toast hellow = Toast.makeText(getApplicationContext(),"안녕하세요",Toast.LENGTH_LONG);
                      items.get(getAdapterPosition()).getName();
//                    String a = items.get(getAdapterPosition()).getName();

                }
            });
        }
    }


    @Override
    public MyviewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_calendar_recycle,parent,false);
        return new MyviewHoler(v);
    }

    @Override
    public void onBindViewHolder( final MyviewHoler holder, final int position) {
        final MyviewHoler myviewHoler = holder;
        myviewHoler.calview.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
