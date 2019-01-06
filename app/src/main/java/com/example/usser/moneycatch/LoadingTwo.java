package com.example.usser.moneycatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class LoadingTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_two);

        ImageView imageView = (ImageView)findViewById(R.id.imageView4);
        imageView.setImageResource(R.drawable.loading2);

        int permissioncheck = ContextCompat.checkSelfPermission(LoadingTwo.this, Manifest.permission.RECEIVE_SMS); // 사용자에게 권한을 묻는 것 int에 담기
        if(permissioncheck == PackageManager.PERMISSION_DENIED){ // DENIED는 권한이 없을 때
            ActivityCompat.requestPermissions(LoadingTwo.this,new String[]{Manifest.permission.RECEIVE_SMS},0);
        }else{ //권한이 있다면 카메라 앱 작동
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }, 1000);
        }




//        ImageView imageView = (ImageView)findViewById(R.id.imageView4);
//        imageView.setImageResource(R.drawable.daum);
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
    }
}
