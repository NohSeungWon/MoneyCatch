package com.example.usser.moneycatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class Itemallview extends AppCompatActivity {

    // 메인 하단 리사이클러뷰 생성 하는 곳
    RecyclerView input_recyclerview; // 입금쪽 리사이클러뷰 생성
    RecyclerView input_minus_recyclerview; // 지출쪽 리사이클러뷰 생성
    RecyclerView.LayoutManager manager_input; // 입금쪽 레이아웃 매니저 생성
    RecyclerView.LayoutManager manager_input_minus; // 지출쪽 리사이클러뷰 매니저 생성
    ArrayList<Mainrecycleitem> item_main = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언
    ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>(); // 메인 하단 사용자 지출 배열 선언
//    ArrayList<Fake_mainrecycleitem> item_main_fake = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언
    // 메인 하단 리사이클러뷰 생성 하는 곳 끝

    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

    Refresh refresh = new Refresh(); // 정렬하기 앤싱크테스크 선언

    private void saveinput() { // 사용자가 입력하고 난 후 배열로 저장된 수입 금액을 쉐어드에 저장한다.
        SharedPreferences share_input = getSharedPreferences("share_input", 0); // 키값으로 share_input을 가진 쉐어드 share_input 선언
        SharedPreferences.Editor editor = share_input.edit(); // 쉐어드를 편집하게 한다
        Gson gson = new Gson(); // 지슨을 gson으로 사용한다고 선언
        String json = gson.toJson(item_main); // 문자열의 형태로 변환
        editor.putString("input",json); // shared_input에 input 이라는 키로 변환된 문자열을 저장
        editor.apply(); // 쉐어드 세이브
    }

    private void loadbreakdown() { // 수입쪽 json으로 저장된 데이터를 배열에 대입시킨다.
        SharedPreferences share_input = getSharedPreferences("share_input", 0); // 키값으로 share_input을 가진 쉐어 share_breakdown 선언3
        Gson gson = new Gson(); // 지슨 gson으로 사용한다고 선언
        String json = share_input.getString("input", null); //sharedbreakdown 안에 input이라는 키값으로 들어있는 데이터를 문자열 변수에 저장
        Type type = new TypeToken<ArrayList<Mainrecycleitem>>(){}.getType(); // 배열을 타입으로 변환 ???? 잘 모르겠다.
        item_main = gson.fromJson(json,type); // item_main이라는 배열을 제이슨을 사용해서 값을 대입시킨다.
        if (item_main == null) { // 만약 배열 값이 아무것도 없을 때
            item_main = new ArrayList<>(); // 배열을 새로만든다.?
        }
    }

    private void saveinput_minus() { // 사용자가 입력하고 난 후 배열로 저장된 지출 금액을 쉐어드에 저장한다.
        SharedPreferences share_input_minus = getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 생성
        SharedPreferences.Editor editor = share_input_minus.edit(); //share_input_minus의 편집자 생성
        Gson gson = new Gson(); // Gson 을 앞으로 gson으로 사용하겠다
        String json = gson.toJson(item_main_minus); // item_main_minus를 문자열로 바꾼다.
        editor.putString("input",json); // 바꾼 문자열을 input이라는 키값으로 집어넣는다.
        editor.apply(); // 저장한다.
    }

    private void loadbreakdown_minus() { // 지출쪽
        SharedPreferences share_input_minus = getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 불러온다.
        Gson gson = new Gson(); // Gson gson으로 쓰겠다.
        String json = share_input_minus.getString("input", null); // input이라는 키값으로 저장된 데이터를 문자열에 담는다.
        Type type = new TypeToken<ArrayList<Mainrecycleitem_minus>>(){}.getType(); // 어레이리스트의 타입을 type로 쓴다. 잘모르겠다??
        item_main_minus = gson.fromJson(json,type); //어레이 리스트는 gson으로부터 가져온 형태와 같다 = 값을 선언한 것과 같다.
        if (item_main_minus == null) { // 만약에 어레이리스트의 값이 없다면
            item_main_minus = new ArrayList<>(); // 새롭게 만든다.
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemallview);

        refresh.execute(); // 정렬 앤싱크 실행

        loadbreakdown();
        loadbreakdown_minus();

        input_recyclerview = findViewById(R.id.all_add_recycle_view); // 수입쪽 리사이클러뷰 연결
        input_minus_recyclerview = findViewById(R.id.all_minus_recycle_view); // 지출 쪽 리사이클러뷰 연결

        manager_input = new LinearLayoutManager(this); // 선언된 수입 쪽 매니저를 라이너레이아웃으로 셋팅한다.
        manager_input_minus = new LinearLayoutManager(this); // 선언된 지출 쪽 매니저를 라이너레이아웃으로 셋팅한다.
        input_minus_recyclerview.setLayoutManager(manager_input_minus); //  지출 쪽 리사이클러뷰어 레이아웃을  매니저로 셋팅한다.
        input_recyclerview.setLayoutManager(manager_input); // 입금 쪽 리사이클러뷰 레이아웃을 매니저로 셋팅한다.


        final Adapter_itemall_add adapter_itemall_add = new Adapter_itemall_add(item_main); // 어댑터를 배열로 셋팅한다.
        Adater_itemall_minus adater_itemall_minus = new Adater_itemall_minus(item_main_minus);

        //뷰에 어답터 셋팅
        input_recyclerview.setAdapter(adapter_itemall_add);
        input_minus_recyclerview.setAdapter(adater_itemall_minus);

        Button back = (Button)findViewById(R.id.back_all); // 메인으로 돌아가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        //         리사이클러뷰가 계속 클리되는 문제를 위해 게스터디텍터로 한 번 클릭만 하게 한다.
        final GestureDetector gestureDetector = new GestureDetector(Itemallview.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


//         하단 수입부분 클릭리스너 ,  클릭하면 포지션에 해당하는 값들을 fix 액티비티에 전달해준다.
        input_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                ArrayList<Mainrecycleitem> item = new ArrayList<>();
                if (child != null && gestureDetector.onTouchEvent(e)) { // 중복클릭안되게 하는 것
//                  final TextView calclick =(TextView) rv.getChildViewHolder(child).itemView.findViewById(R.id.cal_view);
//                  Toast.makeText(getApplication(), calclick.getText().toString(), Toast.LENGTH_SHORT).show();
//                  TextView cal = (TextView)findViewById(R.id.dialog_cal_view);
                    int potision = rv.getChildAdapterPosition(child); // 클릭시 리사이클러뷰 포지션 값을 가져온다
                    int input = item_main.get(potision).getName();
                    String breakdown = item_main.get(potision).getBreakdown();
                    String cal = item_main.get(potision).getCal();
                    String url = item_main.get(potision).getUrl();
                    String time = item_main.get(potision).getTime();

                    Intent passfix = new Intent(getApplicationContext(), Fix.class);
                    passfix.putExtra("cal", cal);
                    passfix.putExtra("input", input);
                    passfix.putExtra("breakdown", breakdown);
                    passfix.putExtra("position", potision);
                    passfix.putExtra("uri", url);
                    passfix.putExtra("time",time);
                    startActivity(passfix);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

//         하단 지출 리사이클러뷰를 수정할 수 있는 fix_minus로 연결하는 리스너
        input_minus_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                ArrayList<Mainrecycleitem> item = new ArrayList<>();
                if (child != null && gestureDetector.onTouchEvent(e)) {
//                  final TextView calclick =(TextView) rv.getChildViewHolder(child).itemView.findViewById(R.id.cal_view);
//                  Toast.makeText(getApplication(), calclick.getText().toString(), Toast.LENGTH_SHORT).show();
//                  TextView cal = (TextView)findViewById(R.id.dialog_cal_view);
                    int potision = rv.getChildAdapterPosition(child); // 클릭시 리사이클러뷰 포지션 값을 가져온다
                    int input = item_main_minus.get(potision).getName();
                    String breakdown = item_main_minus.get(potision).getBreakdown();
                    String cal = item_main_minus.get(potision).getCal();
                    String url = item_main_minus.get(potision).geturl_minus();
                    String time = item_main_minus.get(potision).getTime();

                    Intent passfix_minus = new Intent(getApplicationContext(), Fix_minus.class);

                    passfix_minus.putExtra("cal", cal);
                    passfix_minus.putExtra("input", input);
                    passfix_minus.putExtra("breakdown", breakdown);
                    passfix_minus.putExtra("position", potision);
                    passfix_minus.putExtra("url", url);
                    passfix_minus.putExtra("time",time);

                    startActivity(passfix_minus);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });


    } //oncreate 끝

    /**
     * 입금 정렬 메소드 부분
     */

     // 입금 부분 최신순 정렬
    class Change_cal_up_add implements Comparator<Mainrecycleitem>{
        @Override
        public int compare(Mainrecycleitem t, Mainrecycleitem t1) {
            return t1.getCal().compareTo(t.getCal());
        }
    }
    // 입금 부분 오래된 순 정렬
    class Change_cal_down_add implements Comparator<Mainrecycleitem>{
        @Override
        public int compare(Mainrecycleitem t, Mainrecycleitem t1) {
            return t.getCal().compareTo(t1.getCal());
        }
    }

    /**
     * 지출 정렬 메소드 부분
     */

    // 지출 부분 최신순 정렬
    class Change_cal_up_minus implements Comparator<Mainrecycleitem_minus>{
        @Override
        public int compare(Mainrecycleitem_minus t, Mainrecycleitem_minus t1) {
            return t1.getCal().compareTo(t.getCal());
        }
    }
    // 지출 부분 오래된 순 정렬
    class Change_cal_down_minus implements Comparator<Mainrecycleitem_minus>{
        @Override
        public int compare(Mainrecycleitem_minus t, Mainrecycleitem_minus t1) {
            return t.getCal().compareTo(t1.getCal());
        }
    }


    public class Refresh extends AsyncTask<Integer, Integer, Integer>{ //3초 광고 앤싱크

        @Override
        protected void onPreExecute() { // 백그라운드를 실행하기전에 메인쓰레드에서 선행작업
            super.onPreExecute();
        }

        @Override // 백그라운드에서 실행할 작업들을 해결하는 곳
        protected Integer doInBackground(Integer... Integers) {

//            Log.e("로그","백그라운드 실행 되었음");

            /**
             * 수익 스피너
             */

            String[] add_spiner; // 수익 부분 지출 어레이 선언
            add_spiner = new String[]{"최신","과거"}; // 내용 입력
            // 어답터로 셋팅
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    add_spiner);
            final Spinner spinner_add = (Spinner)findViewById(R.id.spiner_add); // 스피너 아이디값 매칭
            spinner_add.setAdapter(adapter); // 스피너를 어답터로 셋팅
            spinner_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    int value = (int) spinner_add.getItemIdAtPosition(position); // 스피너 클릭시 해당 포지션 값 담기

                    if (value == 0) { publishProgress(1);  }
                    if (value == 1) { publishProgress(2);  }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {  }
            });

            /**
             * 지출 스피너
             */

            String[] minus_spiner; // 수익 부분 지출 어레이 선언
            minus_spiner = new String[]{"최신","과거"}; // 내용 입력
            // 어답터로 셋팅
            ArrayAdapter<String> adapter_minus = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    minus_spiner);
            final Spinner spinner_minus = (Spinner)findViewById(R.id.spiner_minus); // 스피너 아이디값 매칭
            spinner_minus.setAdapter(adapter_minus); // 스피너를 어답터로 셋팅
            spinner_minus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    int value = (int) spinner_minus.getItemIdAtPosition(position); // 스피너 클릭시 해당 포지션 값 담기

                    if (value == 0) { publishProgress(3); }
                    if (value == 1) { publishProgress(4); }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... a) { // 백그라운드 진행상황을 뷰에 알릴 수 있음
            super.onProgressUpdate();

            Adapter_itemall_add adapter_itemall_add = new Adapter_itemall_add(item_main); // 어댑터를 배열로 셋팅한다.
            Adater_itemall_minus adater_itemall_minus = new Adater_itemall_minus(item_main_minus);
            //뷰에 어답터 셋팅
            input_recyclerview.setAdapter(adapter_itemall_add);
            input_minus_recyclerview.setAdapter(adater_itemall_minus);

            if (a[0]==1) // 수익 최신순 눌렀을 때
            {
                Change_cal_up_add change_cal_up_add = new Change_cal_up_add();
                Collections.sort(item_main,change_cal_up_add);
//                item_main.add(new Mainrecycleitem(0,"dd",null,null));
                adapter_itemall_add.notifyDataSetChanged();
//                refresh.cancel(true);
            }
            if (a[0]==2) // 수익 오래된 순 눌렀을 때
            {
                Change_cal_down_add change_cal_down_add = new Change_cal_down_add();
                Collections.sort(item_main,change_cal_down_add);
//                item_main.add(new Mainrecycleitem(0,"dd",null,null));
                adapter_itemall_add.notifyDataSetChanged();
//                refresh.cancel(true);
            }
            if (a[0]==3) // 지출 최신순 눌렀을 때
            {
                Change_cal_up_minus change_cal_up_minus = new Change_cal_up_minus();
                Collections.sort(item_main_minus,change_cal_up_minus);
//                item_main.add(new Mainrecycleitem(0,"dd",null,null));
                adater_itemall_minus.notifyDataSetChanged();
            }
            if (a[0]==4) // 지출 오래된 순 눌렀을 때
            {
                Change_cal_down_minus change_cal_down_minus_minus = new Change_cal_down_minus();
                Collections.sort(item_main_minus,change_cal_down_minus_minus);
//                item_main.add(new Mainrecycleitem(0,"dd",null,null));
                adater_itemall_minus.notifyDataSetChanged();
            }

        }
        @Override
        protected void onPostExecute(Integer result) { // 백그라운드 결과를 뷰에 알릴 수 있음
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled(){
            super.onCancelled();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        refresh.cancel(true);
        saveinput();
        saveinput_minus();
    }


}
