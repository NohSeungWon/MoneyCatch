package com.example.usser.moneycatch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FirstviewActivity extends Activity {

    String Tag = "퍼스트 액티비티"; // 로그 검색용
    ImageButton self;
    ImageButton call;
    ImageButton finish;
    ImageButton start;

//    void dialogshow(){  // 다이얼로그 만들기
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("나는 광고창이지롱");
//        builder.setMessage("약오르지 광고나와서");
//        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.show();
//
//    } // 다이얼로그 종료

    void addialogshow(){  // 다이얼로그 만들기
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("2번 뒤로가기하면 나온다");
        builder.setMessage("나왔으니까 이제 안나온다");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    } // 다이얼로그 종료


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstview);




        self = (ImageButton)findViewById(R.id.camerabtn); // 카메라버튼
        call = (ImageButton) findViewById(R.id.callbtn); // 전화버튼

        finish = (ImageButton) findViewById(R.id.finishbtn); // no 버튼
        start = (ImageButton) findViewById(R.id.startbtn); // Yes 버튼

        final Toast hellow = Toast.makeText(getApplicationContext(),"안녕하세요",Toast.LENGTH_LONG);



        start.setOnClickListener(new View.OnClickListener() { // 시작하기 버튼을 누르면 메인 액티비티 띄우기
            @Override
            public void onClick(View view) { // yes 버튼 누를시 메인 액티비티 전환
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                hellow.show();
                onPause();
                // hellow.setGravity(Gravity.TOP,0,0);  토스트 위치값 조정
            }

        }); //startbtn 끝

        finish.setOnClickListener(new View.OnClickListener() { // 종료하기 버튼 누르면 앱이 종료되기
            @Override
            public void onClick(View view) { // No 버튼 누를시 앱 종료

               onPause();
               android.os.Process.killProcess(android.os.Process.myPid());
               moveTaskToBack(true);

            }
        });// finish 끝


        self.setOnClickListener(new View.OnClickListener() { //카메라 앱 구동
            @Override
            public void onClick(View view) { // 카메라 앱 불러오기
                // 사용자에게 권한 묻기

                int permissioncheck = ContextCompat.checkSelfPermission(FirstviewActivity.this, Manifest.permission.CAMERA);
                if(permissioncheck== PackageManager.PERMISSION_DENIED){ //DENIED는 권한이 없을 때
                    ActivityCompat.requestPermissions(FirstviewActivity.this,new String[]{Manifest.permission.CAMERA},0);
                }else{ //권한이 있다면 카메라 앱 작동
                    Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(picture);
                }

            }
        }); // 카메라 끝

        call.setOnClickListener(new View.OnClickListener() { // 전화걸기 앱으로 연결
            @Override
            public void onClick(View view) { // 전화앱 실행시키기
                Intent call = new Intent(Intent.ACTION_DIAL);
                startActivity(call);
            }
        }); // 전화 앱 끝

        Log.e(Tag,"oncreate");
    } //ocreate 끝

//    int a = 0;

    @Override
    protected void onStart() {
        super.onStart();

//        a++;
//        if (a==3){
//            addialogshow();
//        }

        Log.e(Tag,"onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag,"onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag,"onpause");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag,"onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag,"ondestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(Tag,"onrestart");
    }



} // 메인 끝
