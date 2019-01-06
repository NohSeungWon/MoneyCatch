package com.example.usser.moneycatch;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.Menu.NONE;

public class Adater_add extends RecyclerView.Adapter<Adater_add.MyviewHolder> implements Itemtouchhelperlistner{
//
        static ArrayList<Addrecycleitem> addrecycleitems;
        Adater_add(ArrayList<Addrecycleitem> addrecycleitems){
        this.addrecycleitems = addrecycleitems;
        }
        final static Adater_add adater_add = new Adater_add(addrecycleitems);


        // 컨텍스트 메뉴 사용시 RecyclerView.ViewHolder 를 상속받은 클래스 내에서 리스너를 선언해야한다.
//       static Adater_add adater_add ;
        Context mcontext;


        public class MyviewHolder extends RecyclerView.ViewHolder {
            // 어레이 리스트가 바뀌면 변할 값들 선언
            CheckBox addlist;
            Button edit;


            public MyviewHolder(final View itemView) {
                super(itemView);
                addlist = itemView.findViewById(R.id.addlistbtn);
                edit = itemView.findViewById(R.id.editbtn);

//                itemView.setOnCreateContextMenuListener(this); // 리스너를 현재 클래스에서 구현한다고 설정해둠
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { // 수정버튼
                        final AlertDialog.Builder add = new AlertDialog.Builder(view.getContext());
                        final EditText editText = new EditText(view.getContext());
                        add.setTitle("내역을 수정하세요");
                        editText.setText(addrecycleitems.get(getAdapterPosition()).getName()); // 에딧 텍스트에 배열 값 셋팅하기
                        add.setView(editText);
                        add.setPositiveButton("입력완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = editText.getText().toString();
                                Addrecycleitem a = new Addrecycleitem(name); // 생성자 선언
                                addrecycleitems.set(getAdapterPosition(),a); // 선언된 생성자로 배열 변경된 name 값을 셋팅
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
        if(fromPosition < 0 || fromPosition >= addrecycleitems.size() || toPosition < 0 || toPosition >= addrecycleitems.size()){
            return false;
        }

        Addrecycleitem fromItem = addrecycleitems.get(fromPosition);
        addrecycleitems.remove(fromPosition);
        addrecycleitems.add(toPosition, fromItem);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemRemove(int position) {
        addrecycleitems.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public MyviewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addrecycle,parent,false);
        return new MyviewHolder(v);
    }


    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        final MyviewHolder myviewHolder =(MyviewHolder) holder;
        myviewHolder.addlist.setText(addrecycleitems.get(position).getName());


}

    public int getItemCount() {
        return addrecycleitems.size();
    }


}
