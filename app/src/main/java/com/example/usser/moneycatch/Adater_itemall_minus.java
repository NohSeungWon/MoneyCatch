package com.example.usser.moneycatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adater_itemall_minus extends RecyclerView.Adapter<Adater_itemall_minus.Myviewholder> {

    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

    static ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>();
    Adater_itemall_minus(ArrayList<Mainrecycleitem_minus> item_main_minus){
        this.item_main_minus = item_main_minus;
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        Button breakdown;
        Button user_input;
        TextView cal_view;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            breakdown = itemView.findViewById(R.id.breakdown_view_all_minus);
            user_input = itemView.findViewById(R.id.userinput_view_all_minus);
            cal_view = itemView.findViewById(R.id.cal_view_all_minus);
        }
    }


    @Override
    public Myviewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_itemallveiw_minus_recycle,parent,false);
        return new Adater_itemall_minus.Myviewholder(v);
    }

    @Override
    public void onBindViewHolder( Myviewholder holder, int position) {
        Adater_itemall_minus.Myviewholder myviewholder = (Adater_itemall_minus.Myviewholder) holder;
        String getName = comma.format(item_main_minus.get(position).getName());
        myviewholder.breakdown.setText(item_main_minus.get(position).getBreakdown());
        myviewholder.user_input.setText(getName+"원");
        myviewholder.cal_view.setText(item_main_minus.get(position).getCal());
    }

    @Override
    public int getItemCount() {
        return item_main_minus.size();
    }

}
