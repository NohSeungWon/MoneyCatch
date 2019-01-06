package com.example.usser.moneycatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Context context;

    // 메인 하단 리사이클러뷰 생성 하는 곳
    RecyclerView input_recyclerview; // 입금쪽 리사이클러뷰 생성
    RecyclerView input_minus_recyclerview; // 지출쪽 리사이클러뷰 생성
    RecyclerView.LayoutManager manager_input; // 입금쪽 레이아웃 매니저 생성
    RecyclerView.LayoutManager manager_input_minus; // 지출쪽 리사이클러뷰 매니저 생성
    ArrayList<Mainrecycleitem> item_main = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언
    ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>(); // 메인 하단 사용자 지출 배열 선언
    // 메인 하단 리사이클러뷰 생성 하는 곳 끝

    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

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

//    Re_ad re = new Re_ad(); // 3초 마다 광고 실행 앤싱크 선언
    Up up = new Up(); // 해당하는 날짜에 값 찾아서 리시이클러뷰에 뿌려주는 앤싱크

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 캘린더뷰의 현재 년도와 날짜의 해당하는 값만 나오게 하기 위해
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M");
        SimpleDateFormat simpleDateFormat_month = new SimpleDateFormat("MM");
        String time = simpleDateFormat.format(date); // 현재 날짜 문자열로 담기)
        String month = simpleDateFormat_month.format(date); // 현재 날짜 문자열로 담기

//        Log.e("로그","현재 날짜"+time);

        CalendarView cal = (CalendarView) findViewById(R.id.calendarView);

//        re.execute(1); // 3초 광고 실행
        up.execute(); // 날짜 클릭하면 일치하는 내역 값만 뜨게 하는 앤싱크
//        loadbreakdown(); // 배열 값 쉐어에 저장 수입쪽
//        loadbreakdown_minus(); // 지출쪽 저장\


        /**
         * 메인 하단 리사이클러뷰 선언
         */
        input_recyclerview = findViewById(R.id.main_input_cal_recycle); // 수입쪽 리사이클러뷰 연결
        input_minus_recyclerview = findViewById(R.id.main_input_cal_recycle_minus); // 지출 쪽 리사이클러뷰 연결

        manager_input = new LinearLayoutManager(this); // 선언된 수입 쪽 매니저를 라이너레이아웃으로 셋팅한다.
        manager_input_minus = new LinearLayoutManager(this); // 선언된 지출 쪽 매니저를 라이너레이아웃으로 셋팅한다.
        input_minus_recyclerview.setLayoutManager(manager_input_minus); //  지출 쪽 리사이클러뷰어 레이아웃을  매니저로 셋팅한다.
        input_recyclerview.setLayoutManager(manager_input); // 입금 쪽 리사이클러뷰 레이아웃을 매니저로 셋팅한다.

        // 배열 어답터에 셋팅해주고

        final Adater_main_input adater_main_input = new Adater_main_input(item_main); // 어댑터를 배열로 셋팅한다.
        Adater_main_input_minus adater_main_input_minus = new Adater_main_input_minus(item_main_minus);

        //뷰에 어답터 셋팅
        input_recyclerview.setAdapter(adater_main_input);
        input_minus_recyclerview.setAdapter(adater_main_input_minus);


        // 날짜를 선택하지 않고 플러스 버튼을 눌렀을 경우
        // 플러스 버튼 shake효과와 toast로 잘못된 사용임을 알린다.
        final ImageButton addbutton = (ImageButton) findViewById(R.id.plusbtn);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this, "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                addbutton.startAnimation(shake);
            }
        });


        // 인텐트 입력값 받기
        final Intent intent = getIntent();

        //  입금시 입력 금액, 내역, 날짜 값 배열에 넣기위한 작업
        int add_input = intent.getIntExtra("plus_input", 0); // plus_input이라는 키로 넘어온 값을 int로 담는다
        String add_breakdown = intent.getStringExtra("plus_breakdown_input"); // plus_breakdown_input이라는 키로 넘어온 값을 string에 담는다.
        String add_cal = intent.getStringExtra("plus_call_input"); // plus_call_input이라는 키로 넘어온 값을 string에 담는다.
        String add_url = intent.getStringExtra("add_url"); // url 넘어온 값 담기
        String add_time = intent.getStringExtra("add_time"); // 몇 년 몇 월 데이터인지 구분하는 값 스트링 담기


        // 지출시 입력 내역 값 배열에 넣기위한 작업
        int minus_input = intent.getIntExtra("minus_input", 0); //minus_input 이라는 키로 넘어온 값을 int로 담는다.
        String minus_breakdown = intent.getStringExtra("minus_breakdown_input"); // minus_breakdown 이라는 키로 넘어온 값을 string으로 담는다.
        String minus_cal = intent.getStringExtra("minus_cal"); // minus_cal라는 키로 넘어온 값을 string으로 담는다.
        String minus_url = intent.getStringExtra("minus_url"); // 사진 스트링 담기
        String minus_time = intent.getStringExtra("minus_time"); // 몇 년 몇 월 데이터인지 구분하는 값 스트링 담기
        Log.e("로그","유알엘 없으면 어떻게 오지"+minus_url);
        // 입금에서 넘어온 값인지 , 지출에서 넘어온 값인지 구별하기 위함
        int add_check = intent.getIntExtra("add_check", 0); // add_check 로 넘어온 값을 int에 담는다.
        int minus_check = intent.getIntExtra("minus_check", 0); // minus_check로 넘어온 값을 int에 담는다.


        if (add_check == 1)
        { // 수입에서 넘어올 때 캐치
//          Log.e("get"," 입금시 넘어오는 값 이건 넘어오지마 ->"+ add_input);
            item_main.add(new Mainrecycleitem(add_input, add_breakdown, add_cal, add_url,add_time)); // 위에서 저장한 값들을 어레이리스트에 추가시킨다.
            adater_main_input.notifyDataSetChanged(); // 어답터에게 데이터가 변했다는 사실을 알리고 새로고침 시킨다.
            saveinput();

        }
        else if (minus_check == 2)
        { // 지출에서 넘어오는 값 캐치
//            Log.e("get","제대로 넘어 와라 이것아 ->"+ minus_input);
            item_main_minus.add(new Mainrecycleitem_minus(minus_input, minus_breakdown, minus_cal, minus_url,minus_time));  // 위에서 저장한 값들을 어레이리스트에 추가시킨다.
            adater_main_input_minus.notifyDataSetChanged(); // 어댑터에 데이터가 변했다는 것을 알림
            saveinput_minus();
//            Log.e("tag", "지출에서 넘어온 금액" + minus_input);
//            Log.e("tag", "지출에서 넘어온 내역 " + minus_breakdown);
//            Log.e("tag", "날짜 시바 좀 떠라" + minus_cal);
        }


        // 메인에서 수입, 지출, 잔액 나타내는 상단 부분 쉐어로 입력하기 위해 선언하는 곳
        TextView addview = (TextView) findViewById(R.id.addview); // 수입금액 표현 뷰
        final TextView minusview = (TextView) findViewById(R.id.minusview); // 지출금액 표현 뷰
        TextView currentview = (TextView) findViewById(R.id.currentview); // 잔액금액 표현 뷰

        // 쉐어 선언하는 곳
        SharedPreferences share_add_view = getSharedPreferences("plus_get", 0); // 입금
        SharedPreferences share_minus_view = getSharedPreferences("minus_get", 0); // 지출
        SharedPreferences share_current_view = getSharedPreferences("current_get", 0); // 잔액

        // 쉐어 에딧트 선언하는 곳
        final SharedPreferences.Editor edit_share_add = share_add_view.edit();  //입금
        final SharedPreferences.Editor edit_share_minus = share_minus_view.edit();  // 지출
        final SharedPreferences.Editor edit_share_current = share_current_view.edit(); // 잔액

        // 초기화 버튼
        Button reset = (Button) findViewById(R.id.resetbtn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // 자동 새로고침 되는 방법은 뭘까
                edit_share_add.clear();
                edit_share_add.apply();
                edit_share_current.clear();
                edit_share_current.apply();
                edit_share_minus.clear();
                edit_share_minus.apply();

                SharedPreferences share_input_minus = getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 생성
                SharedPreferences.Editor editor = share_input_minus.edit(); //share_input_minus의 편집자 생성
                editor.clear();
                editor.apply();
                saveinput_minus();

                SharedPreferences share_input = getSharedPreferences("share_input", 0); // 키값으로 share_input을 가진 쉐어드 share_input 선언
                SharedPreferences.Editor editor2 = share_input.edit(); // 쉐어드를 편집하게 한다
                editor2.clear();
                editor2.apply();
                saveinput();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 인텐트로 넘어온 입금, 지출 값들을 변수에 담는 곳
        final int plusinputdata = intent.getIntExtra("plusinput", 0); // 지출금액
        int minusinputdata = intent.getIntExtra("minusinput", 0); // 마이너스금액

        // 사용자가 입금 / 지출을 입력하고 완료한 경우
        if (plusinputdata > 0)
        { // 입금에서 값을 입력한 경우
//            Log.e("get", "입금 액티비티에서 입력완료를 눌렀을 때 ㅡ> 넘어온 값 : " + plusinputdata);
            int sum_add = share_add_view.getInt("add", 0) + plusinputdata;
            edit_share_add.putInt("add", sum_add); // 입금
            edit_share_add.apply(); // 입금 저장
        }
        if (minusinputdata > 0)
        { // 지출에서 값을 입력한 경우
//            Log.e("get", "지출 액티비티에서 입력완료를 눌렀을 때 ㅡ> 넘어온 값 : " + minusinputdata);
            int sum_minus = share_minus_view.getInt("minus", 0) + minusinputdata;
            edit_share_minus.putInt("minus", sum_minus); // 지출
            edit_share_minus.apply();
        }
        int c = share_add_view.getInt("add", 0);

//        private void save_up_input() { // 사용자가 입력하고 난 후 배열로 저장된 지출 금액을 쉐어드에 저장한다.
//            SharedPreferences share_minus_view = getSharedPreferences("minus_get", 0); // 지출 // 키 값으로 share_input_minus를 가진 쉐어 생성
//            SharedPreferences.Editor editor = share_minus_view.edit(); //share_input_minus의 편집자 생성
//            Gson gson = new Gson(); // Gson 을 앞으로 gson으로 사용하겠다
//            String json = gson.toJson(item_main_minus); // item_main_minus를 문자열로 바꾼다.
//            editor.putString("input",json); // 바꾼 문자열을 input이라는 키값으로 집어넣는다.
//            editor.apply(); // 저장한다.
//        }
//
//        private void load_up_input() { // 지출쪽
//            SharedPreferences share_input_minus = getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 불러온다.
//            Gson gson = new Gson(); // Gson gson으로 쓰겠다.
//            String json = share_input_minus.getString("input", null); // input이라는 키값으로 저장된 데이터를 문자열에 담는다.
//            Type type = new TypeToken<ArrayList<Mainrecycleitem_minus>>(){}.getType(); // 어레이리스트의 타입을 type로 쓴다. 잘모르겠다??
//            item_main_minus = gson.fromJson(json,type); //어레이 리스트는 gson으로부터 가져온 형태와 같다 = 값을 선언한 것과 같다.
//            if (item_main_minus == null) { // 만약에 어레이리스트의 값이 없다면
//                item_main_minus = new ArrayList<>(); // 새롭게 만든다.
//            }
//        }


        Button allview = (Button)findViewById(R.id.goall); // 전체 내역보고 수정할 수 있는 액티비티로 이동
        allview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),Itemallview.class);
                startActivity(intent);
            }
        });

        Button combine = (Button)findViewById(R.id.combine);
        combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Statistic.class);
                startActivity(intent);
            }
        });


        /**
         * 그래프 시작점 (월간 지출 리포트)
         */
        PieChart pieChart; // 파이 차트를 쓴다고 전역에 알림
        pieChart = (PieChart) findViewById(R.id.piechart); // 파이차트 아이디 값 매칭

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.GREEN);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>(); // % 뷰로 포현될 어레이리스트 선언

        // 뷰로 표현될 어레이에 값 넣는 곳

        // 내역을 모두 불러와서 중복을 제거한다.

        loadbreakdown_minus(); // 지출 어레이 리스트 불러온다
//        Log.e("로그","나는 로드 시킨 후에 0번 값"+item_main_minus.get(0).getName()+item_main_minus.get(0).getTime());
//        Log.e("로그","나는 로드 시킨 후에 1번 값"+item_main_minus.get(1).getName()+item_main_minus.get(1).getTime());

        ArrayList<String> ii = new ArrayList<>(); // 지출 내역만 담을 어레이리스트
        ArrayList<String> ii2 = new ArrayList<>(); // 지출 내역의 중복 값을 제거하고 담은 어레이 리스트

        // 지출 내역 새로 담기
        for (int a=0; a<item_main_minus.size(); a++)
        {
//            Log.e("로그","지출 어레이 사이즈 "+item_main_minus.size());
            if (time.equals(item_main_minus.get(a).getTime())) // 현재 날짜 값에 맞는 데이터 골라내기
            {
//                Log.e("로그","나는 포문 다음에 나오는 값"+item_main_minus.get(a).getName());
                String sum = item_main_minus.get(a).getBreakdown();
                ii.add(sum);
            }
        }

        // 중복 값 제거하고 다시 배열에 담기
        for (int a = 0 ; a < ii.size(); a++)
        {
//            Log.e("로구","i2 배열 실행됨");
            if (!ii2.contains(ii.get(a))) // ii2 배열에 ii.get(a) 값이 없으면
            {
                ii2.add(ii.get(a)); // 없는 그 값을 추가해라
//                Log.e("롷ㄱ","ii에 내역넣기ㄱ이 들어가면 오케이"+ii2.get(0));
            }
        }

        // 파이 엔트리에 넣기
        if (ii2.size() == 0) // 지출 내역이 아무것도 없을 때
        {
            yValues.add(new PieEntry(1, "지출없음"));
//            Log.e("로그","배열이 0 인데 값은?"+yValues.get(0));
        }

//        Log.e("로그","현재 날짜 값"+time);
        for (int a=0; a<ii2.size(); a++) // 지출 내역이 있을 때
        {
            int value = 0; // 0으로 다시 초기화 시켜 각 개별 값을 value에 담기
            // 0으로 초기화 시키지 않으면 전체 지출 값이 value로 설정하게 됨
            String breakdown = ii2.get(a); // 중복 값 제거 된 내역 순차적 불러오기
            for (int b =0; b< item_main_minus.size(); b++)
            {
                if (time.equals(item_main_minus.get(b).getTime()))  // 현재 달력날에 있는 값만 매칭시키기
                {
//                    Log.e("로그","포문안에 있는 배열의 타임 값"+item_main_minus.get(b).getTime());
                    if (breakdown.equals(item_main_minus.get(b).getBreakdown())) // 일치하는 내역 값 매칭시키기
                    {
                        value += item_main_minus.get(b).getName(); // 매칭값에 금액을 다 담기
                    }
                }
            }
            yValues.add(new PieEntry(value, breakdown)); // 내역 + 같은 내역을 가지고 있는 값들의 합을 넣기
        }
        pieChart.notifyDataSetChanged(); // 데이터 변경알림

        // 무슨 데이터인지 알려주는 설명서 선언
        Description description = new Description();
        description.setText(month+"월 지출 리포트(%)"); //라벨
        description.setTextSize(10);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);  //JOYFUL_COLORS

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GREEN); // 원형 안에 뜨는 텍스트 색깔

        pieChart.setData(data);

        /**
         * 그래프 끝 (월간 지출 리포트)
         */

        // 메인 하단 월 지출 총계

        TextView minus_sum = (TextView)findViewById(R.id.minus_sum);
        minus_sum.setText(month+"월 지출 총계 : 0원"); // 지출이 아무것도 없을 때 셋팅

        for (int a=0; a<ii2.size(); a++) // 지출 내역이 있을 때
        {
            int value = 0; // 0으로 다시 초기화 시켜 각 개별 값을 value에 담기
            // 0으로 초기화 시키지 않으면 전체 지출 값이 value로 설정하게 됨
            String breakdown = ii2.get(a); // 중복 값 제거 된 내역 순차적 불러오기
            for (int b =0; b< item_main_minus.size(); b++)
            {
                if (time.equals(item_main_minus.get(b).getTime()))  // 현재 달력날에 있는 값만 매칭시키기
                {
//                    Log.e("로그","포문안에 있는 배열의 타임 값"+item_main_minus.get(b).getTime());
                    if (breakdown.equals(item_main_minus.get(b).getBreakdown())) // 일치하는 내역 값 매칭시키기
                    {
                        value += item_main_minus.get(b).getName(); // 매칭값에 금액을 다 담기
                    }
                }
            }
            String format = comma.format(value);
            minus_sum.setText(month+"월 총 지출 : "+value+"원");
        }


        //// 외부 앱실행할 때 사용하는 소스 계산기 불러왔었음
//        Button callculatorbtton = (Button) findViewById(R.id.Calculatorbtn); // 외부 계산기앱 실행버튼
//        callculatorbtton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { // 외부 계산기 앱 불러오기
//                Intent intent = getPackageManager().getLaunchIntentForPackage("com.softdx.calculator");
//                startActivity(intent);
//                onPause();
//            }
//        });


        Log.e("로그", "oncreate");
    }  // oncreate 끝

    /**
     * oncreate 끝나는 지점!!!!
     */
    public class Up extends AsyncTask<String,String,String>{ // 날짜를 클릭하면 해당하는 값이 나오게 하는 앤싱크
        // 클릭하지 않았을 때 내역이 없습니다를 settext한다.
        // 날짜를 클릭하면 해당하는 내역이 뜨게 한다.
        // 클릭한 날짜에 값이 없다면 내역이 없습니다를 뜨게 한다.

        @Override
        protected void onPreExecute() { // 백그라운드 실행 전 뷰 처리
            super.onPreExecute();
            // 처음 화면은 내역 없음을 뷰에 처리해둔다.
            loadbreakdown_minus();
            loadbreakdown();
//            Log.e("로그","온프리익스큐트실행됨을 알립니다.");
//            item_main.add(new Mainrecycleitem(0,"없음",null,null));
//            item_main_minus.add(new Mainrecycleitem_minus(0,"없음",null,null));
        }

        @Override
        protected String doInBackground(String... strings)
        { // 백그라운드에서 실행하는 것

            final int sum= item_main.size() + item_main_minus.size(); // 어레이리스트와 사용자의 날짜 값이 맞는게 없을 경우 포문을 돌리기 위한 변하지 않는 값 선언

            CalendarView cal = (CalendarView) findViewById(R.id.calendarView);
            cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                    Calendar calendar;

                    final String choice = year + "/" + (month + 1) + "/" + day; // 클릭한 날짜 값 스트링으로 담기

//                Toast.makeText(MainActivity.this, choice, Toast.LENGTH_SHORT).show();
                    // 일단 쉐어드 값을 불러온다.
                    loadbreakdown();
                    loadbreakdown_minus();

                    // 이 싯팔진짜 개고생해서 만들었다
                    // ㅠㅠ 사용자가 클릭한 날짜와 배열에 들어 있는 날짜가 일치하는 게 있으면 그것만 뷰에 보여준다.
                    // 일치하는게 없으면 0원 처리한다
                    // 문제점이 뭐였는지 정의하기도 너무 힘든데 ..
                    // 처음 로직은 일치하는 값들을 찾고, 뒤에 없다면 0원 처리를 하는거였는데
                    // 포문으로 값이 매칭될 때 까지 찾다보니 뒤에 0원 처리에 모든 값이 걸려
                    // 전부 0원 처리가 되었다.
                    // 그래서 매칭되지 않는 것부터 포문을 시작하니 뒤에서 처리가 되었다
                    // 무슨 말인지 말로 쓰니까 하나도 모르지만 아무튼 그렇다..

                    // 입금 부분에서 일치하는 값이 없을 경우
                    for (int a =0; a<item_main.size(); a++){
                        if (!(item_main.get(a).getCal().equals(choice))) //
                        {
//                            Log.e("로그","입금에서 일치하지 않음"+"// 선택 날짜 :"+choice);
                            publishProgress("","","","","nothing"); // 일치하지 않을 때 처리]
//                            break;
                        }
                    }

                    // 지출부분에서 일치하는 값이 없을 경우
                    for (int a =0; a<item_main_minus.size(); a++){
                        if (!(item_main_minus.get(a).getCal().equals(choice)))
                        {
//                            Log.e("로그","지출에서 일치하지 않음"+"// 선택 날짜 :"+choice);
                            publishProgress("","","","","nothing"); // 일치하지 않을 때 처리
//                            break;
                        }
                    }

                    // 수입과 지출 중에 일치하는 값이 있을 경우
                    // 둘다 일치하는 경우
                    Loopbreak:
                    for (int a =0; a<item_main.size(); a++){  // + 일치
                        if (item_main.get(a).getCal().equals(choice))
                        {
//                            Log.e("로그","수입에서 일치할 경우"+"// 선택 날짜 :"+choice);
                            int add_value = item_main.get(a).getName();
                            String add_breakdown = item_main.get(a).getBreakdown();
                            String add_cal = item_main.get(a).getCal();
                            String add_url = item_main.get(a).getUrl();
//                            Log.e("로그", "수입에서 일치할 경우 에서 매칭 값 " +"// 매칭 값 : "+add_cal);
                            for (int c = 0; c < item_main_minus.size(); c++) // + 일치 하고 -도 일치 하는게 있을 때
                            {
                                if (item_main_minus.get(c).getCal().equals(choice))  // 지출 배열과 사용자의 값이 일치 할 때
                                {
//                                    Log.e("로그","수입을 걸쳐, 지출도 일치할 경우"+"// 선택 날짜 :"+choice);
                                    // 지출 배열에 입력 할 것
                                    int minus_value = item_main_minus.get(c).getName();
                                    String minus_breakdown = item_main_minus.get(c).getBreakdown();
                                    String minus_cal = item_main_minus.get(c).getCal();
//                                    Log.e("로그","수입을 걸쳐, 지출도 일치할 경우"+"// 매칭 값 : "+minus_cal);
                                    String minus_url = item_main_minus.get(c).geturl_minus();
                                    publishProgress(Integer.toString(add_value), add_breakdown, add_cal, add_url,
                                            "add_both",
                                            Integer.toString(minus_value), minus_breakdown, minus_cal, minus_url);
                                    break Loopbreak;
                                }
                            }
                            publishProgress(Integer.toString(add_value), add_breakdown, add_cal, add_url, "go");
                        }
                        else // - 일치
                        {
                            for (int b=0; b<item_main_minus.size(); b++)
                            {
                                if (item_main_minus.get(b).getCal().equals(choice))
                                {
//                                    Log.e("로그", "마이너스가 일치할경우"+"// 선택 날짜 :"+choice);
                                    int value = item_main_minus.get(b).getName();
                                    String breakdown = item_main_minus.get(b).getBreakdown();
                                    String cal = item_main_minus.get(b).getCal();
//                                    Log.e("로그", "마이너스가 일치할 경우 에서 매칭 값 "+"// 매칭 값 :"+cal);
                                    String url = item_main_minus.get(b).geturl_minus();
                                    publishProgress(Integer.toString(value), breakdown, cal, url, "go_minus");
//                                    break Loopbreak;
                                }
                            }
                        }
                    }

                    final ImageButton addbutton = (ImageButton) findViewById(R.id.plusbtn);
                    addbutton.setOnClickListener(new View.OnClickListener() { // 날짜와 함께 다음화면 으로 넘어가기
                        @Override
                        public void onClick(View view) { // + 버튼을 눌렀을 때 수입 입력 화면으로 넘아가기
                            // 메인 액티비티에서 사용자가 터치한 달력날짜를 인텐트로 넘긴다.
                            Intent intent = new Intent(getApplicationContext(), Plusactivity.class);
                            intent.putExtra("cal", choice);
                            startActivity(intent);
                        }
                    });
                }
            });
            return null;  // 리턴값을 설정하면 post로 감
        }

        @Override
        protected void onProgressUpdate(String... values) { // 백그라운드 진행상황을 뷰에 알릴 수 있음
            super.onProgressUpdate(values);
            Adater_main_input adater_main_input = new Adater_main_input(item_main); // 어댑터를 배열로 셋팅한다.
            Adater_main_input_minus adater_main_input_minus = new Adater_main_input_minus(item_main_minus);

            input_recyclerview.setAdapter(adater_main_input);
            input_minus_recyclerview.setAdapter(adater_main_input_minus);

//            TextView cal = (TextView)findViewById(R.id.cal_view);
//            Button input = (Button)findViewById(R.id.userinput_view);
//            Button breakdown = (Button)findViewById(R.id.breakdown_view);
//            cal.setText(values[2]);
//            input.setText(values[0]+"원");
//            breakdown.setText(values[1]);

            if (values[4]=="go") // 수입 클릭에서 넘어올 때
            {
//                Log.e("로그","수입만 있을 때 실행");
//                Log.e("로그","날짜"+values[2]);
//                Log.e("로그","내역"+values[1]);
//                Log.e("로그","금액"+values[0]);
//                Log.e("로그","유알아이"+values[3]);
                item_main.clear(); // 어레이리스트 값 초기화
                item_main_minus.clear();
                item_main_minus.add(new Mainrecycleitem_minus(0, "없음", null, null,null));
                item_main.add(new Mainrecycleitem(Integer.parseInt(values[0]), values[1], values[2], values[3],null)); // 백그라운드에서 넘어온 값으로 배열 셋팅
//                Log.e("로그", "나는 초기화 후의 어레이 값이다"+item_main.get(0).getCal());
                adater_main_input.notifyDataSetChanged();
                adater_main_input_minus.notifyDataSetChanged();
            }
            if (values[4]=="go_minus") // 지출 클릭해서 넘어올 때
            {
//                Log.e("로그"," 지출만 있을 때 실행");
                item_main_minus.clear(); // 어레이리스트 값 초기화
                item_main.clear();
                item_main.add(new Mainrecycleitem(0, "없음", null, null,null));
                item_main_minus.add(new Mainrecycleitem_minus(Integer.parseInt(values[0]), values[1], values[2], values[3],null)); // 백그라운드에서 넘어온 값으로 배열 셋팅
//                Log.e("로그", "나는 초기화 후의 어레이 값이다"+item_main_minus.get(0).getCal());
                adater_main_input_minus.notifyDataSetChanged();
                adater_main_input.notifyDataSetChanged();
            }
            if (values[4] == "add_both")
            { // 지출 포문에서 넘어올 때
//                Log.e("로그","둘다 있을 때 실행");
                item_main_minus.clear(); // 어레이리스트 값 초기화
                item_main.clear();
                item_main.add(new Mainrecycleitem(Integer.parseInt(values[0]), values[1], values[2], values[3],null));
                item_main_minus.add(new Mainrecycleitem_minus(Integer.parseInt(values[5]), values[6], values[7], values[8],null)); // 백그라운드에서 넘어온 값으로 배열 셋팅
//                Log.e("로그", "나는 초기화 후의 어레이 값이다"+item_main_minus.get(0).getCal());
                adater_main_input_minus.notifyDataSetChanged();
                adater_main_input.notifyDataSetChanged();
            }
            if (values[4] == "nothing"){
//                Log.e("로그","나띵실행");
                item_main_minus.clear(); // 어레이리스트 값 초기화
                item_main.clear();
                item_main.add(new Mainrecycleitem(0, "없음", null, null,null));
                item_main_minus.add(new Mainrecycleitem_minus(0, "없음", null, null,null));
                adater_main_input_minus.notifyDataSetChanged();
                adater_main_input.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPostExecute(String s) { // 백그라운드가 끝나고 리턴을 던지면 뷰 처리
            super.onPostExecute(s);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public class Re_ad extends AsyncTask<Integer, Integer, Integer>{ //3초 광고 앤싱크

        @Override
        protected void onPreExecute() { // 백그라운드를 실행하기전에 메인쓰레드에서 선행작업
            super.onPreExecute();
        ImageView ad = (ImageView)findViewById(R.id.ad);
        ad.setImageResource(R.drawable.google); // 구글 광고로 첫 셋팅을 한다.
        }

        @Override // 백그라운드에서 실행할 작업들을 해결하는 곳
        protected Integer doInBackground(Integer... Integers) {
            // oncreate 상황에서만 되게
            // onpause 상황이 되면 꺼지게


            int max = 3; // 이미지 개수
                try {
                        while (Integers[0]!=4){ // 4가 아닐 때는 계속?
                        for (int a = 0; a < max; a++) { // 0,1,2 가 될 때 까지 포문 돌리기
                            Thread.sleep(3000); // 3초 딜레이
                            publishProgress(a); // 3초마다 0에서 1씩 증가하는 값을 업데이트에 보냄
                                if (a == 2) { // a가 2가 되면 a를 1로 만들기
                                 a -= 2; // 2를 감소시켜라 원래는 2씩 감소지만 어짜피 0이되니까 무한이 될거야
                                  break;
                                   }
//                                   Log.e("계속 넘어가는 중","숫자가 계속 리셋되지 "+a);
                            }if (isCancelled()) break;
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
        }

        @Override
        protected void onProgressUpdate(Integer... a) { // 백그라운드 진행상황을 뷰에 알릴 수 있음
            super.onProgressUpdate();
            ImageView ad = (ImageView)findViewById(R.id.ad);
            int [] img = {R.drawable.naver, R.drawable.daum,R.drawable.instagram};
            ad.setImageResource(img[a[0]]); // 여기서 a[0]은 백그라운드에서 넘어온 값인듯
        }
        @Override
        protected void onPostExecute(Integer result) { // 백그라운드 결과를 뷰에 알릴 수 있음
            super.onPostExecute(result);
        }
        @Override
        protected void onCancelled(){
            super.onCancelled();
        }
    }   // 3초광고 앤싱크 끝



    @Override
    protected void onStart() {
        super.onStart();

        // 사용자가 입력값을 입력했을 때 값 업데이트 하기위해 onstart에 넣음

        TextView addview = (TextView) findViewById(R.id.addview); // 수입금액 표현 뷰
        TextView minusview = (TextView) findViewById(R.id.minusview); // 지출금액 표현 뷰
        TextView currentview = (TextView) findViewById(R.id.currentview); // 잔액금액 표현 뷰

        // 쉐어 선언하는 곳
        // 이곳에 선언되는 키값은 어떨 때 사용해야 하나? * 클래스형태일때 값을 불러 올 수 있는 것 같다..
        SharedPreferences share_add_view = getSharedPreferences("plus_get", 0); // 입금
        SharedPreferences share_minus_view = getSharedPreferences("minus_get", 0); // 지출
        SharedPreferences share_current_view = getSharedPreferences("current_get", 0); // 잔액
        int add = share_add_view.getInt("add", 0);
        int minus = share_minus_view.getInt("minus", 0);
        String format_add = comma.format(add);
        String format_minus = comma.format(minus);
        // 값 텍스트에 셋 해주는 곳
        addview.setText(format_add + "원"); // 입금
        minusview.setText(format_minus + "원"); // 지출
        // 잔액 쉐어에 입금액 - 지출액을 셋팅시켜주는 곳
        int currentdata = add - minus;
        String format_current = comma.format(currentdata);
//        Log.e("get","값아 넘어와라 아주 잘 -> "+currentdata);
        currentview.setText(format_current + "원");


        Log.e("로그", "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("로그", "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        saveinput();
//        saveinput_minus();
//        re.cancel(true); // 3초마다 갱신되는 광고판 멈추기
        up.cancel(true);
//        Log.e("로그","싱크테스크 죽음");
        Log.e("로그", "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        up.cancel(true);
//        Log.e("로그","싱크테스크 죽음");
        Log.e("로그", "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("로그", "ondestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        up.cancel(true);
//        Log.e("로그","싱크테스크 죽음");
        Log.e("로그", "onrestart");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

//    private long time= 0;
//    @Override
//    public void onBackPressed(){
//        if(System.currentTimeMillis()-time>=2000){
//            time=System.currentTimeMillis();
//            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
//        }else if(System.currentTimeMillis()-time<2000){
//            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
//
//        }
//    }



