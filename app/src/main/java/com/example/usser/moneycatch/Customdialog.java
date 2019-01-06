//package com.example.usser.moneycatch;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//public class Customdialog extends Dialog implements View.OnClickListener {
//
//    private Dialoglistner dialoglistner;
//
//    public void setDialoglistner(Dialoglistner dialoglistner){
//        this.dialoglistner = dialoglistner;
//    }
//
//    public Customdialog( Context context) {
//        super(context);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.custondailog);
//
//        TextView cal = (TextView)findViewById(R.id.cal_view); // 날짜 뷰
//        EditText breakdown = (EditText)findViewById(R.id.dialog_breakdown);
//        EditText input = (EditText)findViewById(R.id.dialog_input);
//        Button next = (Button)findViewById(R.id.next);  // 입력완료 버튼
//
//        next.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//}
