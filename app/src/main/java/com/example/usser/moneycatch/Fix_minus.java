package com.example.usser.moneycatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Fix_minus extends AppCompatActivity {

    ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언 // 수정하는데 사용될 리스트 선언
    final Adater_main_input_minus adater_main_input = new Adater_main_input_minus(item_main_minus); // 어댑터를 배열로 셋팅한다.

    private void saveinput_minus(){ // 사용자가 입력하고 난 후 배열로 저장된 지출 금액을 쉐어드에 저장한다.
        SharedPreferences share_input_minus = getSharedPreferences("share_input_minus",0); // 키 값으로 share_input_minus를 가진 쉐어 생성
        SharedPreferences.Editor editor = share_input_minus.edit(); //share_input_minus의 편집자 생성
        Gson gson = new Gson(); // Gson 을 앞으로 gson으로 사용하겠다
        String json = gson.toJson(item_main_minus); // item_main_minus를 문자열로 바꾼다.
        editor.putString("input",json); // 바꾼 문자열을 input이라는 키값으로 집어넣는다.
        editor.apply(); // 저장한다.
    }
    private void loadbreakdown_minus(){
        SharedPreferences share_input_minus = getSharedPreferences("share_input_minus",0); // 키 값으로 share_input_minus를 가진 쉐어 불러온다.
        Gson gson = new Gson(); // Gson gson으로 쓰겠다.
        String json = share_input_minus.getString("input",null); // input이라는 키값으로 저장된 데이터를 문자열에 담는다.
        Type type = new TypeToken<ArrayList<Mainrecycleitem_minus>>() {}.getType(); // 어레이리스트의 타입을 type로 쓴다. 잘모르겠다??
        item_main_minus = gson.fromJson(json,type); //어레이 리스트는 gson으로부터 가져온 형태와 같다 = 값을 선언한 것과 같다.
        if (item_main_minus == null){ // 만약에 어레이리스트의 값이 없다면
            item_main_minus = new ArrayList<>(); // 새롭게 만든다.
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_minus);

        loadbreakdown_minus();

        final TextView cal_textview = (TextView)findViewById(R.id.dialog_cal_view_fixminus);
        final EditText breakdown_edit = (EditText)findViewById(R.id.dialog_breakdown_fixminus);
        final EditText input = (EditText)findViewById(R.id.dialog_input_fixminus);
        ImageView imageView = (ImageView)findViewById(R.id.minus_url) ;
        Button next = (Button)findViewById(R.id.next_fixminus);
        Button delete = (Button)findViewById(R.id.delete_minus);

        Intent get = getIntent();

        int get_input = get.getIntExtra("input",0);
        String get_breakdown = get.getStringExtra("breakdown");
        String get_cal = get.getStringExtra("cal");
        final int get_position = get.getIntExtra("position",0); // 몇 번쨰 어레이인지 확인하게 해줌
        final String get_url = get.getStringExtra("url"); // 사진 스트링 값
        Uri myUri = Uri.parse(get_url); // 스트링 패스값 uri로 저장
        final String get_time = get.getStringExtra("time");

        cal_textview.setText(get_cal);
        breakdown_edit.setText(get_breakdown);
        input.setText(Integer.toString(get_input));
        imageView.setImageURI(myUri);

        final SharedPreferences share_minus_view = getSharedPreferences("minus_get",0); // 지출 // 입금 쉐어드 불러오기
        final SharedPreferences.Editor editor = share_minus_view.edit(); // 불러온 쉐어 에딧트 선언
        // 방금 액티비티로 넘어온 시점에서 쉐어드 값을 조회하고 인트 넣기
        final int now_input = share_minus_view.getInt("minus",0); // add 라는 키값으로 담겨있는 데이터 int에 넣기


        String breakdown = breakdown_edit.getText().toString(); // 수정하기 전 내역 값 변수 담기
        final int name = Integer.parseInt(input.getText().toString()); // 수정하기 전 금액 값 변수 담기
        String cal = cal_textview.getText().toString(); // 수정하기 전 날짜 값 변수 담기

        // 값이 제대로 나오는지 확인하기 위한 로그용
//        String a = item_main_minus.get(get_position).getBreakdown();
//        int b  = item_main_minus.get(get_position).getName();
//        String c = item_main_minus.get(get_position).getCal();
//        Log.e("tag","넘어오오오야냐냐" + get_position);
//        Log.e("tag","너가 값이 분명히 있을텐데 말이야 "+c+a+b);


        delete.setOnClickListener(new View.OnClickListener() { // 완전 삭제 할 때
            @Override
            public void onClick(View view) {
                // 포지션에 해당하는 값들 다 삭제 하고 저장
                // 쉐어드는 해당하는 값 그만큼을 제거
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                int name_fix = Integer.parseInt(input.getText().toString()); // 금액 값 변수 담기

                int sum_minus = share_minus_view.getInt("minus",0) - name_fix; // 그 금액만큼 쉐어드에서 뺀다.
                editor.putInt("minus",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                editor.apply(); // 저장한다.

                item_main_minus.remove(get_position); // 넘어온 포지션 값으로 해당하는 값을 제거
                adater_main_input.notifyDataSetChanged(); // 변경값 어댑터에 알림
                saveinput_minus(); // 변경사항 쉐어드에 저장
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // 온클릭 시점에서 변경 값들을 체크하려면 다시 선언해야 할 것 같다. 오 맞다!!!
                String breakdown_fix = breakdown_edit.getText().toString(); // 내역 값 변수 담기
                int name_fix = Integer.parseInt(input.getText().toString()); // 금액 값 변수 담기
                String cal_fix = cal_textview.getText().toString(); // 날짜 값 변수 담기
                String url_fix = get_url;


                if (name == name_fix){ // 수정 전 금액과 클릭리스너를 실행한 시점에 금액이 !!! 같다면
                    Mainrecycleitem_minus change = new Mainrecycleitem_minus(name_fix,breakdown_fix,cal_fix,url_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main_minus.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput_minus();
                    startActivity(intent);
                } else if(name > name_fix){ // 수정 전  금액이 리스너를 실행한 값보다 !!! 크다면
                    // 쉐어 값이 현재 500이고   넘어온 값이 100 , 수정한 값이 20이라면  80이 차이나니까  쉐어 값에서 80을 뺀다.
                    int fix_minus = name - name_fix; // 수정 전 금액에서 수정한 금액을 뺀다.
                    int sum_minus = share_minus_view.getInt("minus",0) -  fix_minus; // 그 금액만큼 쉐어드에서 뺀다.
                    editor.putInt("minus",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                    editor.apply(); // 저장한다.
                    Mainrecycleitem_minus change = new Mainrecycleitem_minus(name_fix,breakdown_fix,cal_fix,url_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main_minus.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput_minus();
                    startActivity(intent);
                } else if (name < name_fix){ // 최초시점 금액이 리스너를 실행한 값보다 !!! 작으면
                    // 쉐어값이 현재 500, 넘어온 값 100, 수정한 값 120 이면 차이는 20 쉐어에서 20을 더한다.
                    int fix_minus = name_fix - name; // 수정 전 금액에서 수정한 금액을 뺀다.
                    int sum_minus = share_minus_view.getInt("minus",0) +  fix_minus; // 그 금액만큼 쉐어드에 더한다..
                    editor.putInt("minus",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                    editor.apply(); // 저장한다.
                    Mainrecycleitem_minus change = new Mainrecycleitem_minus(name_fix,breakdown_fix,cal_fix,url_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main_minus.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput_minus();
                    startActivity(intent);
                }

            }
        });

    }





}
