package com.example.usser.moneycatch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adater_main_input extends RecyclerView.Adapter<Adater_main_input.MyviewHoler>  {
      Context context;
//     사용할 배열 선언
    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌
    static ArrayList<Mainrecycleitem> item_main; // 사용할 입금 배열 객체  item_main 으로 선언
    Adater_main_input( ArrayList<Mainrecycleitem> item_main){
        this.item_main = item_main;
    }

    public class MyviewHoler extends RecyclerView.ViewHolder {
       final Button breakdown;
       final Button user_input;
       final TextView cal_view;
//       final Button fix;
        public MyviewHoler(View view) {
            super(view);
            breakdown = itemView.findViewById(R.id.breakdown_view);
            user_input = itemView.findViewById(R.id.userinput_view);
            cal_view = itemView.findViewById(R.id.cal_view);

        }
    }

    @Override
    public MyviewHoler onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_input_recycle,parent,false);
        return new Adater_main_input.MyviewHoler(v);
    }

    @Override
    public void onBindViewHolder( MyviewHoler holder, int position) {
        final Adater_main_input.MyviewHoler myviewHolder =(Adater_main_input.MyviewHoler) holder;
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

