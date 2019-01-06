package com.example.usser.moneycatch;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Statistic extends AppCompatActivity {

    ArrayList<Mainrecycleitem> item_main = new ArrayList<>(); // 메인 하단 사용자 입급 배열 선언
    ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>(); // 메인 하단 사용자 지출 배열 선언
    Date date = new Date();
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
    String check_time = simpleDateFormat1.format(date); // 현재 날짜 문자열로 담기

    List<Entry> 지출 = new ArrayList<>();
    List<Entry> 수입 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_statistic);

        loadbreakdown();
        loadbreakdown_minus();

        LineChart chart = (LineChart) findViewById(R.id.chart);

        int twelve = 12; // x 축이 12월 까지니까

        /**
         * 수입 쪽 그래프 값 넣기
         */
        for (int a=0; a<=twelve; a++)
        {
            switch (a)
            {
                case 1:
                    plus_value(a);
                    break;
                case 2:
                    plus_value(a);
                    break;
                case 3:
                    plus_value(a);
                    break;
                case 4:
                    plus_value(a);
                    break;
                case 5:
                    plus_value(a);
                    break;
                case 6:
                    plus_value(a);
                    break;
                case 7:
                    plus_value(a);
                    break;
                case 8:
                    plus_value(a);
                    break;
                case 9:
                    plus_value(a);
                    break;
                case 10:
                    plus_value(a);
                    break;
                case 11:
                    plus_value(a);
                    break;
                case 12:
                    plus_value(a);
                    break;
            }
        }

        /**
         * 지출 쪽 그래프 값 넣기
         */
        for (int a=0; a<=twelve; a++)
        {
            switch (a)
            {
                case 1:
                    minus_vlue(a);
                    break;
                case 2:
                    minus_vlue(a);
                    break;
                case 3:
                    minus_vlue(a);
                    break;
                case 4:
                    minus_vlue(a);
                    break;
                case 5:
                    minus_vlue(a);
                    break;
                case 6:
                    minus_vlue(a);
                    break;
                case 7:
                    minus_vlue(a);
                    break;
                case 8:
                    minus_vlue(a);
                    break;
                case 9:
                    minus_vlue(a);
                    break;
                case 10:
                    minus_vlue(a);
                    break;
                case 11:
                    minus_vlue(a);
                    break;
                case 12:
                    minus_vlue(a);
                    break;
            }
        }

        // 그래픽 관련 처리하는 곳 -수입-
        LineDataSet lineDataSet = new LineDataSet(수입, "수입");
        lineDataSet.setLineWidth(7); // 라인 굵기
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#398cb6")); // 동그라미 부분
        lineDataSet.setCircleColorHole(Color.parseColor("#50d179")); // 동그라미의 핵 부분
        lineDataSet.setColor(Color.parseColor("#50d179")); // 선 색깔

        // 여기는 뭐 아무것도 모르겠는데 그래픽 관련 처리 인 것 같다. -수입-
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        // 그래픽 관련 처리하는 곳 -지출-
        LineDataSet lineDataSet2 = new LineDataSet(지출, "지출");
        lineDataSet2.setLineWidth(7); // 라인 굵기
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.parseColor("#3a2d3a"));
        lineDataSet2.setCircleColorHole(Color.parseColor("#ce0504"));
        lineDataSet2.setColor(Color.parseColor("#ce0504"));

        // 여기는 뭐 아무것도 모르겠는데 그래픽 관련 처리 인 것 같다.-지출-
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet,lineDataSet2);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축을 아래로 보낸다.
        xAxis.setTextColor(Color.BLACK); // x 축 텍스트 색깔을 변경한다.
        xAxis.enableGridDashedLine(1, 1, 0); // 이건 뭐 아무것도 변경이 안되는데?

        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("월별 수입 지출 비교");

//        chart.setDoubleTapToZoomEnabled(false);
//        chart.setDrawGridBackground(false);
        chart.setDescription(description);
        chart.animateY(2000, Easing.EasingOption.EaseInCubic);
        chart.invalidate();
    }


    private void minus_vlue(int a)
    { // 지출쪽
        String s = "/"+Integer.toString(a);
        String current = check_time.concat(s); // concat은 문자열에 문자열을 더해줌
        int come =0;
        for (int b=0; b<item_main_minus.size(); b++) // 사이즈 만큼 돌고
        {
            if (current.equals(item_main_minus.get(b).getTime())) // 돌면서 현재 날짜와 일치하는 값이 있으면 찾고
            {
                come += item_main_minus.get(b).getName(); // 그 값들을 다 더해서
//                Log.e("로그","여긴 왜 있지"+ come);
            }
        }
        지출.add(new Entry(a, come)); // 포문이 끝나면 더해진 값을 넣어라
    }

    private void plus_value(int a)
    { // 수입쪽
        String s = "/"+Integer.toString(a);
        String current = check_time.concat(s);
        int come =0;
        for (int b=0; b<item_main.size(); b++) // 사이즈 만큼 돌고
        {
            if (current.equals(item_main.get(b).getTime())) // 돌면서 현재 날짜와 일치하는 값이 있으면 찾고
            {
                come += item_main.get(b).getName(); // 그 값들을 다 더해서
//                Log.e("로그","값이 왜 없지"+ come);
            }
        }
        수입.add(new Entry(a, come)); // 포문이 끝나면 더해진 값을 넣어라
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
    private void loadbreakdown_minus() { // 지출쪽
        SharedPreferences share_input_minus = getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 불러온다.
        Gson gson = new Gson(); // Gson gson으로 쓰겠다.
        String json = share_input_minus.getString("input", null); // input이라는 키값으로 저장된 데이터를 문자열에 담는다.
        Type type = new TypeToken<ArrayList<Mainrecycleitem_minus>>() {
        }.getType(); // 어레이리스트의 타입을 type로 쓴다. 잘모르겠다??
        item_main_minus = gson.fromJson(json, type); //어레이 리스트는 gson으로부터 가져온 형태와 같다 = 값을 선언한 것과 같다.
        if (item_main_minus == null) { // 만약에 어레이리스트의 값이 없다면
            item_main_minus = new ArrayList<>(); // 새롭게 만든다.
        }
    }

}
