package com.example.usser.moneycatch;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Plusactivity extends AppCompatActivity {

    ImageButton exit;
    Button add;
    Button minus;
    EditText editText;
    Button calculator;

    RecyclerView addrecycleview;
    RecyclerView.LayoutManager addmanager;

    final ArrayList<Addrecycleitem> additemlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plusactivity);


        editText = (EditText)findViewById(R.id.inputbtn);
        editText.setSelection(editText.getText().length()); // 에딧 텍스트의 커서를 맨 끝으로 이동시킴

        final TextView cal_view = (TextView)findViewById(R.id.choice_cal);
        final Calendar cal = Calendar.getInstance();

        calculator = (Button)findViewById(R.id.calculator_btn); // 계산기 모양 누르면 계산기로 연결되게 한다.
        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Calculator.class);
                startActivity(intent);
            }
        });

        Intent getintent = getIntent();

        // 계산기에서 받은 값 edi에 셋하기
        int calculator_value = getintent.getIntExtra("calculator_value",0); // 계산기에서 넘어온 값
        int calculator_check = getintent.getIntExtra("calculator_check",0); // 계산기에서 넘어왔다는 사실을 알리는 체크 값
        if (calculator_check == 20) { // 체크값과 일치할 때 수행해라
            editText.setText(Integer.toString(calculator_value));
        }

        // 메인에서 사용자의 선택달력날짜 값을 받음
        String input_main = getintent.getStringExtra("cal");
        // 받아서 텍스트뷰에 뿌림
        cal_view.setText(input_main);



        exit = (ImageButton)findViewById(R.id.exitbtn); // x를 르면 메인으로 돌아가기
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        add = (Button)findViewById(R.id.input_add_btn);  // 에딧 텍스트의 값을 가지고 플러스 내역 엑티비티로 전환
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                String userinput = editText.getText().toString();  // 여기에 선언해 줘야만 putextra에 넣어짐.... 리스너 밖에 선언이 안됨 static도..
                String add_cal = cal_view.getText().toString(); // 선택 날짜 값 문자열로 넣기
                int userinput_int_add = Integer.parseInt(userinput); // 사용자 입력값을 인트로 변경
                intent.putExtra("plusinput",userinput_int_add);  // 인텐트에 금액입력값을 넣음
                intent.putExtra("add_call",add_cal); // 인텐트에 날짜 입력값 넣기
                startActivity(intent);
            }
        });

        minus = (Button)findViewById(R.id.input_minus_btn); // 에딧 텍스트의 값을 가지고 마이너스 내역 엑티비티로 전환
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Plusactivity.this, MinusActivity.class);
                String userinput = editText.getText().toString();  // 여기에 선언해 줘야만 값이 넘어감....
                String minus_cal = cal_view.getText().toString(); // 사용자 날짜 선택값 담기
                int userinput_int_minus = Integer.parseInt(userinput); // 금액입력값 인트에 담기
                intent.putExtra("minusinput",userinput_int_minus); // 금액입력값 전달
                intent.putExtra("minus_cal",minus_cal); // 날자 입력값 전달
                startActivity(intent);
            }
        });

    }

}
