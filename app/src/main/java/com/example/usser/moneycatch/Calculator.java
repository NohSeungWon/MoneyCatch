package com.example.usser.moneycatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

public class Calculator extends AppCompatActivity {
    EditText edit; // 상단에 숫자값과 결과가 보여질 EditText

    Button division, plus, equal, multi, sub; //연산자, 익명내부클래스
    Button cancel; //익명내부클래스의임시객체


    String number;
    //첫번째 값  -> 연산자 -> 두번째 값 순서로 진행될 때, 첫번째 값을 담아 둘 곳.
    //int나 double형으로 해도 되는데 나는 연산할 때 한번에 Parse 써서 할거라서 그냥 String으로 함.

    int value; //어떤 연산자가 선택되었는지. 각각 연산자에 대한 버튼이 눌리면, value에 0~3까지 값이 들어가서 어떤 놈이 선택되어 저장되었는지 확인 가능하게 했음

    int DIVISION = 0;
    int PLUS = 1;
    int MULTI = 2;
    int SUB = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        edit = (EditText)findViewById(R.id.edit);
        number = "";

        /** 연산자 + - / * = 는 익명 이너 클래스를 사용한다. mListener를 만들어서 연동시켜준다.  **/
        division = (Button)findViewById(R.id.btn_division); // /
        plus = (Button)findViewById(R.id.btn_plus); // +
        equal = (Button)findViewById(R.id.btn_result); // =
        sub = (Button)findViewById(R.id.btn_sub); // -
        multi = (Button)findViewById(R.id.btn_multi); // *

        division.setOnClickListener(mListener);
        plus.setOnClickListener(mListener);
        equal.setOnClickListener(mListener);
        sub.setOnClickListener(mListener);
        multi.setOnClickListener(mListener);

        Button calculator_exit = (Button)findViewById(R.id.calculator_exit); // 취소 버튼을 누르면 전 액티비티로 돌아간다.
        calculator_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exit = new Intent(getApplicationContext(),Plusactivity.class);
                startActivity(exit);
            }
        });
        Button calculator_push = (Button)findViewById(R.id.calculator_push);// 사용자가 계산완료한 값을 인텐트로 넘긴다.
        calculator_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent push = new Intent(getApplicationContext(),Plusactivity.class);
                String get = edit.getText().toString();
                int a = Integer.parseInt(get);
                int check = 20; // 계산기 값이라는 것을 확인하는 용도
                push.putExtra("calculator_value",a);
                push.putExtra("calculator_check",check);
                startActivity(push);
            }
        });



        /** 익명 이너클래스 내부 객체 사용*/
        cancel = (Button)findViewById(R.id.btn_cancel); // C
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                number = "";
                edit.setText("");
            }
        });
    }
    /** 연산자 용 익명 내부 클래스.*/
    Button.OnClickListener mListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_division :
                    number = edit.getText().toString(); // 첫번째로 입력했던 녀석들 저장해둠. editText가 clear될거니까.
                    edit.setText("");//내용물비워주기
                    value = DIVISION;
                    Toast.makeText(Calculator.this, "/", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_plus :
                    number = edit.getText().toString();
                    edit.setText("");//내용물비워주기
                    value = PLUS;
                    Toast.makeText(Calculator.this, "+", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_sub :
                    number = edit.getText().toString();
                    edit.setText("");
                    value = SUB;
                    Toast.makeText(Calculator.this, "-", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_multi :
                    number = edit.getText().toString();
                    edit.setText("");
                    value = MULTI;
                    Toast.makeText(Calculator.this, "*", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_result :
                    if (value == MULTI) {
                        edit.setText("" + (Integer.parseInt(number) * Integer.parseInt(edit.getText().toString())));
                    } else if (value == SUB){
                        edit.setText("" + (Integer.parseInt(number) - Integer.parseInt(edit.getText().toString())));
                    } else if (value == PLUS){
                        edit.setText("" + (Integer.parseInt(number) + Integer.parseInt(edit.getText().toString())));
                    } else if (value == DIVISION){
                        edit.setText("" + (Integer.parseInt(number) / Integer.parseInt(edit.getText().toString())));
                    } //Double.parseDouble
                    number = edit.getText().toString(); // 최종 결과값을 가지고, 바로 다음 연산을 가능하게 하도록 number에 들어가 있는 값을 교체해준다.
                    break;
            }
        }
    };

    // xml 에서 onClick 이벤트를 연동해놓았던 숫자들입니다.
    public void onClick (View v)
    {
        switch(v.getId()){
            case R.id.btn_0 : edit.setText(edit.getText().toString() + 0); break;
            case R.id.btn_1 : edit.setText(edit.getText().toString() + 1); break;
            case R.id.btn_2 : edit.setText(edit.getText().toString() + 2); break;
            case R.id.btn_3 : edit.setText(edit.getText().toString() + 3); break;
            case R.id.btn_4 : edit.setText(edit.getText().toString() + 4); break;
            case R.id.btn_5 : edit.setText(edit.getText().toString() + 5); break;
            case R.id.btn_6 : edit.setText(edit.getText().toString() + 6); break;
            case R.id.btn_7 : edit.setText(edit.getText().toString() + 7); break;
            case R.id.btn_8 : edit.setText(edit.getText().toString() + 8); break;
            case R.id.btn_9 : edit.setText(edit.getText().toString() + 9); break;

        }

    }




}
