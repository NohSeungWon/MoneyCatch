package com.example.usser.moneycatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter_itemall_add extends RecyclerView.Adapter<Adapter_itemall_add.Myviewholder>{
    Context context;
    //     사용할 배열 선언
    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

    static ArrayList<Mainrecycleitem> item_main; // 사용할 입금 배열 객체  item_main 으로 선언
    Adapter_itemall_add( ArrayList<Mainrecycleitem> item_main){
        this.item_main = item_main;
    }

    public class Myviewholder extends RecyclerView.ViewHolder{
        Button breakdown;
        Button user_input;
        TextView cal_view;
        public Myviewholder(View itemView) {
            super(itemView);
            breakdown = itemView.findViewById(R.id.breakdown_view_all);
            user_input = itemView.findViewById(R.id.userinput_view_all);
            cal_view = itemView.findViewById(R.id.cal_view_all);
        }
    }

    @Override
    public Myviewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_itemallveiw_add_recycle,parent,false);
        return new Adapter_itemall_add.Myviewholder(v);
    }

    @Override
    public void onBindViewHolder( Myviewholder holder, int position) {
        final Adapter_itemall_add.Myviewholder myviewHolder =(Adapter_itemall_add.Myviewholder) holder;
        String getName = comma.format(item_main.get(position).getName());
        myviewHolder.breakdown.setText(item_main.get(position).getBreakdown());
        myviewHolder.user_input.setText(getName+"원");
        myviewHolder.cal_view.setText(item_main.get(position).getCal());
    }

    @Override
    public int getItemCount() {
        return item_main.size();
    }



}
