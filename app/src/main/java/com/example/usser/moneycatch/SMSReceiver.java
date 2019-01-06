package com.example.usser.moneycatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {


//    String regex = "[은행]+\\s+\\d"; // 문자형태를 정규표현식으로 셋팅
    String regex = "^\\[?(Web발신)\\]" +  //[Web발신}
        "?\\s?(삼성|하나|기업|은행)?(\\d){4}?[가-힣]{2}?\\s?[(가-힣)]{1}?(\\*){1}?[가-힣]" +
        "?\\s?([0-9,원]*)"; // 은행명3688(카드번호)승인 노*원(이름)

//    String regex2 = "^([0-9,]*원)";

    ArrayList<Mainrecycleitem_minus> item_main_minus = new ArrayList<>(); // 메인 하단 사용자 지출 배열 선언
    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌


    @Override
    public void onReceive(Context context, Intent intent) {

    // 메인 상단 금액 처리 부분 쉐어
    SharedPreferences share_minus_view = context.getSharedPreferences("minus_get", 0); // 지출
    SharedPreferences share_current_view = context.getSharedPreferences("current_get", 0); // 잔액
    // // 메인 상단 금액 처리 부분 쉐어  에딧트 선언
    final SharedPreferences.Editor edit_share_minus = share_minus_view.edit();  // 지출
    final SharedPreferences.Editor edit_share_current = share_current_view.edit(); // 잔액


    SharedPreferences share_input_minus = context.getSharedPreferences("share_input_minus", 0); // 키 값으로 share_input_minus를 가진 쉐어 불러온다.
    SharedPreferences.Editor editor = share_input_minus.edit(); //share_input_minus의 편집자 생성
    Gson gson = new Gson(); // Gson gson으로 쓰겠다.

        // 수신되었을 때 호출되는 콜백 메서드
        // 매개변수 intent의 액션에 방송의 '종류'가 들어있고
        //         필드에는 '추가정보' 가 들어 있습니다.
        // SMS 메시지를 파싱합니다.
        Bundle bundle = intent.getExtras(); // 번들로 인텐트로 넘어온 문자를 저장한다.
        String str = ""; // 출력할 문자열 저장
        if (bundle != null)
        {
            // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨
            // pdus는 프로토콜이라고 한다.
            Object [] pdus = (Object[])bundle.get("pdus");

            SmsMessage [] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str +=
//                        msgs[i].getOriginatingAddress() + // 전화번호
//                         "문자왔음, " +
                        msgs[i].getMessageBody().toString();
//                        +"\n";
//                Toast.makeText(context,str, Toast.LENGTH_LONG).show(); // 리시버가 문자를 받을 수 있는지 체크하려고 토스트로 띄운다.
            }
            Pattern pattern;
            Pattern pattern2;
            Matcher matcher;
            Matcher matcher2;

            pattern = Pattern.compile(regex); // 은행문자형태를 정규표현식으로 패턴을 설정
            matcher = pattern.matcher(str); // 문자를 문자열로 변환한 것에 패턴과 일치하는 지를 대입한다음


            // 아래에서
            if (matcher.find()) // 위에서 설정한 정규표현식과 문자열이 매칭 된다면
            {
//                Log.e("로그","1번 매처 실행됨");


                String match = matcher.group(5); // 금액부분 추출하기

                // 어레이 불러오기
                String json = share_input_minus.getString("input", null); // input이라는 키값으로 저장된 데이터를 문자열에 담는다.
                Type type = new TypeToken<ArrayList<Mainrecycleitem_minus>>(){}.getType(); // 어레이리스트의 타입을 type로 쓴다. 잘모르겠다??
                item_main_minus = gson.fromJson(json,type); //어레이 리스트는 gson으로부터 가져온 형태와 같다 = 값을 선언한 것과 같다.
                if (item_main_minus == null) { // 만약에 어레이리스트의 값이 없다면
                    item_main_minus = new ArrayList<>(); // 새롭게 만든다.
                }

                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd"); // 몇일에 지출했는지
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/M"); // 구분용
                String time = simpleDateFormat.format(date); // 현재 날짜 문자열로 담기
                String check_time = simpleDateFormat1.format(date); // 현재 날짜 문자열로 담기

//                Log.e("록","오늘 월"+check_time);
//                Toast.makeText(context,match, Toast.LENGTH_LONG).show(); // 리시버가 문자를 받을 수 있는지 체크하려고 토스트로 띄운다.
                String clean1 = match.replaceAll("[^0-9]", ""); // 문자열에서 숫자만 남기도록 변환시킨다.
//                Log.e("로그","리플레이스 된 금액만 나온 값"+clean1);
//                 변환된 값을 어레이에 입력시킨다.
                item_main_minus.add(new Mainrecycleitem_minus(Integer.parseInt(clean1), "신용카드", time, "",check_time));

//                 입력시킨 어레이 값을 쉐어드에 저장한다.
                String json2 = gson.toJson(item_main_minus); // item_main_minus를 문자열로 바꾼다.
                editor.putString("input",json2); // 바꾼 문자열을 input이라는 키값으로 집어넣는다.
                editor.apply(); // 저장한다.

                // 메인 상단 부분 지출 쉐어에 값 추가
                int sum_minus = share_minus_view.getInt("minus", 0) + Integer.parseInt(clean1);
                edit_share_minus.putInt("minus", sum_minus); // 지출
                edit_share_minus.apply();

            }
        }

    } // end of onReceive

}
