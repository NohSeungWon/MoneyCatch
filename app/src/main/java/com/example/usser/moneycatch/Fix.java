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

public class Fix extends AppCompatActivity {

    ArrayList<Mainrecycleitem> item_main = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언 // 수정하는데 사용될 리스트 선언
    final Adater_main_input adater_main_input = new Adater_main_input(item_main); // 어댑터를 배열로 셋팅한다.

    private void saveinput(){ // 사용자가 입력하고 난 후 배열로 저장된 수입 금액을 쉐어드에 저장한다.
        SharedPreferences share_input = getSharedPreferences("share_input",0); // 키값으로 share_input을 가진 쉐어드 share_input 선언
        SharedPreferences.Editor editor = share_input.edit(); // 쉐어드를 편집하게 한다
        Gson gson = new Gson(); // 지슨을 gson으로 사용한다고 선언
        String json = gson.toJson(item_main); // 문자열의 형태로 변환
        editor.putString("input",json); // shared_input에 input 이라는 키로 변환된 문자열을 저장
        editor.apply(); // 쉐어드 세이브
    }
    private void loadbreakdown(){ // json으로 저장된 데이터를 배열에 대입시킨다.
        SharedPreferences share_input = getSharedPreferences("share_input",0); // 키값으로 share_input을 가진 쉐어 share_breakdown 선언3
        Gson gson = new Gson(); // 지슨 gson으로 사용한다고 선언
        String json = share_input.getString("input",null); //sharedbreakdown 안에 input이라는 키값으로 들어있는 데이터를 문자열 변수에 저장
        Type type = new TypeToken<ArrayList<Mainrecycleitem>>() {}.getType(); // 배열을 타입으로 변환 ???? 잘 모르겠다.
        item_main = gson.fromJson(json,type); // item_main이라는 배열을 제이슨을 사용해서 값을 대입시킨다.
        if (item_main == null){ // 만약 배열이 아무것도 없을 때
            item_main = new ArrayList<>(); // 배열을 새로만든다.
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);

        loadbreakdown();

        final TextView cal_textview = (TextView)findViewById(R.id.dialog_cal_view);
        final EditText breakdown_edit = (EditText)findViewById(R.id.dialog_breakdown);
        final EditText input = (EditText)findViewById(R.id.dialog_input);
        final ImageView url = (ImageView)findViewById(R.id.add_url);
        Button next = (Button)findViewById(R.id.next);
        Button delete = (Button)findViewById(R.id.delete) ;

        Intent get = getIntent();

        final int get_position = get.getIntExtra("position",0); // 몇 번째 어레이인지 확인하게 해줌

        final int get_input = get.getIntExtra("input",0);
        final String get_breakdown = get.getStringExtra("breakdown");
        final String get_cal = get.getStringExtra("cal");
        final String get_uri = get.getStringExtra("uri");
        Uri myUri = Uri.parse(get_uri); // 스트링 패스값 uri로 저장
        final String get_time = get.getStringExtra("time");

        cal_textview.setText(get_cal);
        breakdown_edit.setText(get_breakdown);
        input.setText(Integer.toString(get_input));
        url.setImageURI(myUri); //변환된 값으로 이미지뷰 셋





        final SharedPreferences share_add_view = getApplicationContext().getSharedPreferences("plus_get",0); // 입금
        final SharedPreferences.Editor editor = share_add_view.edit();
        final int now_input = share_add_view.getInt("add",0); // add 라는 키값으로 담겨있는 데이터 int에 넣기

         String breakdown = breakdown_edit.getText().toString(); // 내역 값 변수 담기
         final int name = Integer.parseInt(input.getText().toString()); // 금액 값 변수 담기
         final String cal = cal_textview.getText().toString(); // 날짜 값 변수 담기

        // 값이 제대로 나오는지 확인하기 위한 로그용
        String a = item_main.get(get_position).getBreakdown();
        int b  = item_main.get(get_position).getName();
        String c = item_main.get(get_position).getCal();
        Log.e("tag","넘어오오오야냐냐" + get_position);
        Log.e("tag","너가 값이 분명히 있을텐데 말이야 "+c+a+b);

        delete.setOnClickListener(new View.OnClickListener() { // 완전 삭제 할 때
            @Override
            public void onClick(View view) {
                // 포지션에 해당하는 값들 다 삭제 하고 저장
                // 쉐어드는 해당하는 값 그만큼을 제거
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                int name_fix = Integer.parseInt(input.getText().toString()); // 금액 값 변수 담기

                int sum_minus = share_add_view.getInt("add",0) - name_fix; // 그 금액만큼 쉐어드에서 뺀다.
                editor.putInt("add",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                editor.apply(); // 저장한다.

                item_main.remove(get_position); // 넘어온 포지션 값으로 해당하는 값을 제거
                adater_main_input.notifyDataSetChanged(); // 변경값 어댑터에 알림
                saveinput(); // 변경사항 쉐어드에 저장
                startActivity(intent);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // 온클릭 시점에서 변경 값들을 체크하려면 아래 값들을 다시 선언해야 할 것 같다. 오 맞다!!!
                String breakdown_fix = breakdown_edit.getText().toString(); // 내역 값 변수 담기
                int name_fix = Integer.parseInt(input.getText().toString()); // 금액 값 변수 담기
                String cal_fix = cal_textview.getText().toString(); // 날짜 값 변수 담기
                String uri_fix = get_uri;


                if (name == name_fix){ // 수정 전 금액과 클릭리스너를 실행한 시점에 금액이 !!! 같다면
                    Mainrecycleitem change = new Mainrecycleitem(name_fix,breakdown_fix,cal_fix,uri_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput(); // 어레이 리스트 제이슨 형태로 저장
                    startActivity(intent);
                } else if(name > name_fix){ // 수정 전  금액이 리스너를 실행한 값보다 !!! 크다면
                    // 쉐어 값이 현재 500이고   넘어온 값이 100 , 수정한 값이 20이라면  80이 차이나니까  쉐어 값에서 80을 뺀다.
                    int fix_minus = name - name_fix; // 수정 전 금액에서 수정한 금액을 뺀다.
                    int sum_minus = share_add_view.getInt("add",0) -  fix_minus; // 그 금액만큼 쉐어드에서 뺀다.
                    editor.putInt("add",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                    editor.apply(); // 저장한다.
                    Mainrecycleitem change = new Mainrecycleitem(name_fix,breakdown_fix,cal_fix,uri_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput(); // 어레이 리스트 제이슨 형태로 저장
                    startActivity(intent);
                } else if (name < name_fix){ // 최초시점 금액이 리스너를 실행한 값보다 !!! 작으면
                    // 쉐어값이 현재 500, 넘어온 값 100, 수정한 값 120 이면 차이는 20 쉐어에서 20을 더한다.
                    int fix_minus = name_fix - name; // 수정 전 금액에서 수정한 금액을 뺀다.
                    int sum_minus = share_add_view.getInt("add",0) +  fix_minus; // 그 금액만큼 쉐어드에 더한다..
                    editor.putInt("add",sum_minus); // 수정된 값을 다시 쉐어드에 넣는다.
                    editor.apply(); // 저장한다.
                    Mainrecycleitem change = new Mainrecycleitem(name_fix,breakdown_fix,cal_fix,uri_fix,get_time); // 바꾸기 위한 생성자 선언
                    item_main.set(get_position,change); // 받은 포지션값에 해당하는 어레이를 사용자가 입력한 값으로 변경
                    adater_main_input.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
                    saveinput(); // 어레이 리스트 제이슨 형태로 저장
                    startActivity(intent);
                }


            }
        });

    }



}
