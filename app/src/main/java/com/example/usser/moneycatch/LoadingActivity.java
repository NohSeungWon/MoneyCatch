package com.example.usser.moneycatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LoadingActivity extends Activity {

    String Tag = "로딩 액티비티";

    // 앱 실행시 로딩 액티비티 띄우고
    //
    // 메인 액티비티 뜨면 지워지고


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 로딩화면 2초간 띄우고
        // 2초뒤 메인화면 전환
        // 메인화면 전환하고 로딩액티비티 종료
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_loading);

            Intent intent = new Intent(getApplicationContext(), LoadingTwo.class);
            startActivity(intent);

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.getMessage();

        }
        finish();
        Log.e(Tag,"oncreate");
    } // oncreate 끝


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(Tag, "onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag, "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "ondestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(Tag, "onrestart");
    }

}