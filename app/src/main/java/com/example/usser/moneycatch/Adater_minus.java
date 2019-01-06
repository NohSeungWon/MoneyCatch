package com.example.usser.moneycatch;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adater_minus extends RecyclerView.Adapter<Adater_minus.MyViewHoler> implements Itemtouchhelperlistner {

    private ArrayList<Minusrecycleitem> minusrecycleitems ;
    Adater_minus(ArrayList<Minusrecycleitem> minusrecycleitems){
        this.minusrecycleitems = minusrecycleitems;
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
    CheckBox minuslist;
    Button edit;


        public MyViewHoler( View itemView) {
            super(itemView);
        minuslist = itemView.findViewById(R.id.minuslistbtn); // 이곳에 리사이클뷰로 내역이 추가 됨
        edit = itemView.findViewById(R.id.minuseditbtn); // 편집버튼
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 편집버튼을 누르면 에딧 텍스트의 선택한 내역이 입력되고 수정하면 반영됨
                    final android.support.v7.app.AlertDialog.Builder add = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                    final EditText editText = new EditText(view.getContext());
                    add.setTitle("내역을 수정하세요");
                    editText.setText(minusrecycleitems.get(getAdapterPosition()).getName());
                    add.setView(editText);
                    add.setPositiveButton("입력완료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = editText.getText().toString();
                            Minusrecycleitem a = new Minusrecycleitem(name);
                            minusrecycleitems.set(getAdapterPosition(),a);
                            notifyItemChanged(getAdapterPosition());
                            dialogInterface.dismiss();
                        }
                    });
                    add.show();
                }

            });


        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < 0 || fromPosition >= minusrecycleitems.size() || toPosition < 0 || toPosition >= minusrecycleitems.size()){
            return false;
        }

        Minusrecycleitem fromItem = minusrecycleitems.get(fromPosition);
        minusrecycleitems.remove(fromPosition);
        minusrecycleitems.add(toPosition, fromItem);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemRemove(int position) {
        minusrecycleitems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHoler onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_minusrecycle,parent,false);
        return new MyViewHoler(v);
    }

    @Override
    public void onBindViewHolder( MyViewHoler holder, int position) {
        MyViewHoler myViewHoler = (MyViewHoler) holder;
        myViewHoler.minuslist.setText(minusrecycleitems.get(position).getName());



    }

    @Override
    public int getItemCount() {
        return minusrecycleitems.size();
    }



}
