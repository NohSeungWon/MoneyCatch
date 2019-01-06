package com.example.usser.moneycatch;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MinusActivity extends AppCompatActivity {

    String Tag = "마이너스 액티비티";
    RecyclerView minusrecycle;  // 리사이클뷰 선언
    RecyclerView.LayoutManager manager; // 레이아웃 매니저 선언

    ArrayList<Minusrecycleitem> minusrecycleitems = new ArrayList<>(); // 배열 선언

    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

    private static final int SELECT_PICTURE = 1; // 앨범에서 사진가져오기
    private static final int Take_picture = 2; // 사진찍기
    private static final int CROP_IMAGE = 3; // 크랍하기

    private Uri photouri;
    private String selectedImagePath;
    private String absolutePath;

    Re_ad re = new Re_ad(); // 3초 마다 광고 실행 앤싱크 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minus);

        re.execute(1); // 3초 광고 실행

        // 몇 년 몇월 데이터인지 구분하기 위해 현자 날짜 값 구하기
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
//        final String time = simpleDateFormat.format(date); // 현재 날짜 문자열로 담기

        minusrecycle = findViewById(R.id.minus_recycle_view); // 선언된 리사클뷰에 xml에 사용된 리사이클뷰 아이디값 적용
        manager = new LinearLayoutManager(this);
        minusrecycle.setLayoutManager(manager); // 리사이클뷰에 레이아웃을 위에 선언된 라이너 레이아웃으로 셋팅해줌


        loadbreakdown(); // 쉐어 저장한거 불러오기

        final Adater_minus adater_minus = new Adater_minus(minusrecycleitems);
        minusrecycle.setAdapter(adater_minus);

        // 스와이프로 선택된 리사이클러뷰 아이템을 삭제
        ItemTouchHelper touchHelper = new ItemTouchHelper(new Itemtouchhelpercallback(adater_minus));
        touchHelper.attachToRecyclerView(minusrecycle);




        Intent inputintent = getIntent();


        final int input = inputintent.getIntExtra("minusinput",0); // 플러스 액티비티에서 넘어온 값 변수에 담기
        final TextView plus_minus_input = (TextView)findViewById(R.id.get_userminusinput); // plus액티비티에서 전달된 금액을 알려주는 뷰
        String formattedStringPrice = comma.format(input);
        plus_minus_input.setText(formattedStringPrice+"원");

        String cal_input = inputintent.getStringExtra("minus_cal"); // 넘어온 달력 날짜 값
        final TextView cal_view = (TextView)findViewById(R.id.minus_cal_view);
        cal_view.setText(cal_input);

//        ImageView imageset = (ImageView)findViewById(R.id.imageView2);
//        SharedPreferences sharedPreferences = getSharedPreferences("minus_uri_save",0); // 쉐어 불러오기
//        final String path = sharedPreferences.getString("minus_uri",""); // 쉐어 값 스트링으로 담아서
//        Uri myUri = Uri.parse(path); // 스트링 패스값 uri로 저장
//        final ImageView image = (ImageView)findViewById(R.id.imageView2); // 이미지뷰 사용할 것 선언
//        image.setImageURI(myUri); // 이미지 뷰에 셋해준다. uri로 셋


        final Button category_add = (Button)findViewById(R.id.Category_minus_btn); // 내역추가 버튼 선언
        category_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 내역추가 버튼 누르면 어레이리스트에 추가되는 다이얼로그
                AlertDialog.Builder add = new AlertDialog.Builder(MinusActivity.this);
                add.setTitle("추가할 내역을 입력하세요");
                final EditText add_edit = new EditText(MinusActivity.this);
                add.setView(add_edit);
                add.setPositiveButton("입력완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 다이얼로그로 입력한 텍스트가 벨류 값으로 어레이리스트에 저장되고 리스트 갱신
                        String value = add_edit.getText().toString();
                        minusrecycleitems.add(new Minusrecycleitem(value));
                        adater_minus.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                add.show();
            }
        }); // 내역추가 버튼 끝


        // 리사이클러뷰가 계속 클리되는 문제를 위해 게스터디텍터로 한 번 클릭만 하게 한다.
        final GestureDetector gestureDetector = new GestureDetector(MinusActivity.this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        minusrecycle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent( RecyclerView rv,  MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e))
                {   // 내역이 셋팅되어 있는 버튼 을 선언한다.
                    // 버튼 값에 텍스트를 가져온다.
                    final CheckBox minus_edit =(CheckBox) rv.getChildViewHolder(child).itemView.findViewById(R.id.minuslistbtn);
                    Toast.makeText(getApplication(), minus_edit.getText().toString(), Toast.LENGTH_SHORT).show();

                    Button mainput = (Button)findViewById(R.id.minus_permu_btn); // 입력완료버튼 누르면 메인액티비티로 값 전달
                    mainput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            // 각각 값들을 리스너 안에다 선언해야 넘어간다.
                            int check = 2 ;
                            int m_userinput = input;  // 사용자 금액 입력값 -> 메인상단
                            int s_m_userinput = input; // 하단 리사이클러뷰로 보낼 값
                            String m_cal_userinput = cal_view.getText().toString(); // 선택 달력날짜 값
                            String m_breakdown_userinput = minus_edit.getText().toString(); // 사용자가 선택한 내역 값
                            SharedPreferences share_uri_minus = getSharedPreferences("add_uri_save",0); // 사진이 저장된 쉐어 선언
                            String minus_uri = share_uri_minus.getString("add_uri",""); // 쉐어 안에 있는 값 넘겨주기 위해 스트링 변수에 넣기


                            StringBuffer stringBuffer = new StringBuffer(m_cal_userinput); // 선택 날짜를 년 월로만 표현하게
                            String time = stringBuffer.substring(0,6); // 뒤에 일자 짜르고 년,월 만 남기기
                            String cut = stringBuffer.substring(5,7); // 10월 ~ 12월 이슈 해결 나중엔 정규표현식으로 하기
                            String 십월 = time+Integer.toString(0);
                            String 십일월 = time+Integer.toString(1);
                            String 십이월 = time+Integer.toString(2);


                            if (cut.equals(Integer.toString(10)))
                            {
//                                Log.e("로그","10월 실행됨");
                                intent.putExtra("minusinput", m_userinput);  // 금액 상단 보내기
                                intent.putExtra("minus_input", s_m_userinput); // 금액 하단 보내기
                                intent.putExtra("minus_breakdown_input", m_breakdown_userinput); // 내역 값
                                intent.putExtra("minus_cal", m_cal_userinput); // 날짜 값
                                intent.putExtra("minus_check", check); // 구분하기 위한 값
                                intent.putExtra("minus_url", minus_uri); // 이미지 값
                                intent.putExtra("minus_time", 십월); // 현재 년월 값


                                SharedPreferences.Editor editor = share_uri_minus.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                            else if (cut.equals(Integer.toString(11)))
                            {
//                                Log.e("로그","11월 실행됨");
                                intent.putExtra("minusinput", m_userinput);  // 금액 상단 보내기
                                intent.putExtra("minus_input", s_m_userinput); // 금액 하단 보내기
                                intent.putExtra("minus_breakdown_input", m_breakdown_userinput); // 내역 값
                                intent.putExtra("minus_cal", m_cal_userinput); // 날짜 값
                                intent.putExtra("minus_check", check); // 구분하기 위한 값
                                intent.putExtra("minus_url", minus_uri); // 이미지 값
                                intent.putExtra("minus_time", 십일월); // 현재 년월 값


                                SharedPreferences.Editor editor = share_uri_minus.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                           else if (cut.equals(Integer.toString(12)))
                            {
//                                Log.e("로그","12월 실행됨");
                                intent.putExtra("minusinput", m_userinput);  // 금액 상단 보내기
                                intent.putExtra("minus_input", s_m_userinput); // 금액 하단 보내기
                                intent.putExtra("minus_breakdown_input", m_breakdown_userinput); // 내역 값
                                intent.putExtra("minus_cal", m_cal_userinput); // 날짜 값
                                intent.putExtra("minus_check", check); // 구분하기 위한 값
                                intent.putExtra("minus_url", minus_uri); // 이미지 값
                                intent.putExtra("minus_time", 십이월); // 현재 년월 값


                                SharedPreferences.Editor editor = share_uri_minus.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                            else
                            {
                                intent.putExtra("minusinput", m_userinput);  // 금액 상단 보내기
                                intent.putExtra("minus_input", s_m_userinput); // 금액 하단 보내기
                                intent.putExtra("minus_breakdown_input", m_breakdown_userinput); // 내역 값
                                intent.putExtra("minus_cal", m_cal_userinput); // 날짜 값
                                intent.putExtra("minus_check", check); // 구분하기 위한 값
                                intent.putExtra("minus_url", minus_uri); // 이미지 값
                                intent.putExtra("minus_time", time); // 현재 년월 값

                                SharedPreferences.Editor editor = share_uri_minus.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }

                        }
                    });
                }
                return false;
            }

            @Override
            public void onTouchEvent( RecyclerView rv,  MotionEvent e) {          }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {            }
        });


        Button backbtn = (Button) findViewById(R.id.Backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 돌아가기 버튼 누를시 메인액티비티로 전환
                // 매인액티비티 전환후 현재 액티비티 종료
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        }); // 돌아가기 끝


        Button picutre = (Button)findViewById(R.id.minus_picture); // 사진찍기 버튼 찾기
        picutre.setOnClickListener(new View.OnClickListener() { // 사진찍기 누르면 이벤트 발생시키는 리스너 만들기
            @Override
            public void onClick(View view) {
                int permissioncheck = ContextCompat.checkSelfPermission(MinusActivity.this, Manifest.permission.CAMERA); // 사용자에게 권한을 묻는 것 int에 담기
                if(permissioncheck == PackageManager.PERMISSION_DENIED){ // DENIED는 권한이 없을 때
                    ActivityCompat.requestPermissions(MinusActivity.this,new String[]{Manifest.permission.CAMERA},0);
                }else{ //권한이 있다면 카메라 앱 작동
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }

//                int permissioncheck = ContextCompat.checkSelfPermission(MinusActivity.this, Manifest.permission.CAMERA); // 사용자에게 권한을 묻는 것 int에 담기
//                if(permissioncheck == PackageManager.PERMISSION_DENIED){ // DENIED는 권한이 없을 때
//                    ActivityCompat.requestPermissions(MinusActivity.this,new String[]{Manifest.permission.CAMERA},0);
//                }else{ //권한이 있다면 카메라 앱 작동
//                    Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivity(picture);
//                    doTakePhotoAction();
//                }
            }
        });

        Button get_picture = (Button)findViewById(R.id.minus_get_picture); // 사진가져오기 버튼 셋팅
        get_picture.setOnClickListener(new View.OnClickListener() { // 사진가져오기 누르면 이벤트 발생 리스너 만들기
            @Override
            public void onClick(View view) {

                int permissionCheck = ContextCompat.checkSelfPermission(MinusActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED){ // 권한이 없다면 동의 묻기
                    ActivityCompat.requestPermissions(MinusActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }else { // 권한이 있을 때
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_PICTURE);
                }

//                int permissionCheck = ContextCompat.checkSelfPermission(MinusActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE);
//                if (permissionCheck == PackageManager.PERMISSION_DENIED){ // 권한이 없다면 동의 묻기
//                    ActivityCompat.requestPermissions(MinusActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
//                }else { // 권한이 있을 때
//                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent,SELECT_PICTURE);
//                    doTakeAlbumAction();
//                }
            }
        });



        Log.e(Tag,"oncreate");
    } // oncreated 끝

    // uri로 이미지 뷰 셋팅
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData(); // uri 받아오기
                String realpath = getRealpath(selectedImageUri); // uri로 절대경로 찾고 스트링으로 변환
                Log.e("uri 값","uri값은 넘어온다.     "+selectedImageUri);
                ImageView image = (ImageView)findViewById(R.id.imageView2); // 이미지뷰 사용할 것 선언
                image.setImageURI(selectedImageUri); // 이미지 뷰에 셋해준다.
                SharedPreferences share_uri_minus = getSharedPreferences("add_uri_save",0);
                SharedPreferences.Editor editor = share_uri_minus.edit();
                editor.putString("add_uri",realpath);
                editor.apply();
                Log.e("절대경로","왜 안넘어올까"+realpath);
            }
        }

    }



    public class Re_ad extends AsyncTask<Integer, Integer, Integer> { //3초 광고 앤싱크

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
                        Log.e("계속 넘어가는 중","숫자가 계속 리셋되지 "+a);
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


    public String getRealpath(Uri uri) { // 사진에서 얻은 uri로 절대 경로 얻기 메소드
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        c.moveToFirst();
        String path = c.getString(index);
        return path;
    }



    @Override
    protected void onStart() {
        super.onStart();
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
        savebreakdown(); // 어레이리스트 쉐어에 저장하기
        re.cancel(true); // 3초마다 갱신되는 광고판 멈추기
        Log.e(Tag,"onpause");
    }

    private void savebreakdown(){
        SharedPreferences minus_share_breakdown = getSharedPreferences("minus_share_breakdown",0);
        SharedPreferences.Editor editor = minus_share_breakdown.edit();
        Gson gson = new Gson();
        String json = gson.toJson(minusrecycleitems);
        editor.putString("breakdown",json);
        editor.apply();
    }
    private void loadbreakdown(){
        SharedPreferences share_breakdown = getSharedPreferences("minus_share_breakdown",0);
        Gson gson = new Gson();
        String json = share_breakdown.getString("breakdown",null);
        Type type = new TypeToken<ArrayList<Minusrecycleitem>>() {}.getType();
        minusrecycleitems = gson.fromJson(json,type);
        if (minusrecycleitems == null){
            minusrecycleitems = new ArrayList<>();
        }
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


//    @Override
//    public void onBackPressed() { //뒤로가기 버튼을 눌렀을 때
//        // 뒤로가기 버튼 누르면
//        // addactivity 정지
//        // mainactivity 띄우기
//        super.onBackPressed();
//        onPause();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//    } // onbackpressed 끝
}
