package com.example.usser.moneycatch;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    String Tag= "에드 액티비티";

    RecyclerView myrecyckle ;
    RecyclerView.LayoutManager manager;
    ArrayList<Addrecycleitem> addrecycleitems = new ArrayList<>();

    private static final int SELECT_PICTURE = 1; // 앨범에서 사진가져오기
    private static final int Take_picture = 2; // 사진찍기
    private static final int CROP_IMAGE = 3; // 크랍하기


    private void savebreakdown(){
        SharedPreferences share_breakdown = getSharedPreferences("share_breakdown",0);
        SharedPreferences.Editor editor = share_breakdown.edit();
        Gson gson = new Gson();
        String json = gson.toJson(addrecycleitems);
        editor.putString("breakdown",json);
        editor.apply();
    }
    private void loadbreakdown(){
        SharedPreferences share_breakdown = getSharedPreferences("share_breakdown",0);
        Gson gson = new Gson();
        String json = share_breakdown.getString("breakdown",null);
        Type type = new TypeToken<ArrayList<Addrecycleitem>>() {}.getType();
        // 어떻게 하면 개별 값에 접근할 수 있을까?
        // = 형태로 집어 넣는 것이 아니라 개별 파라미터 값을 빼내고 집어 넣는게 가능해야 한다,
        addrecycleitems = gson.fromJson(json,type);
        if (addrecycleitems == null){
            addrecycleitems = new ArrayList<>();
        }
    }

    DecimalFormat comma = new DecimalFormat("###,###"); // 숫자 3자리마다 콤마 찍어줌

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        myrecyckle = findViewById(R.id.add_recycle_view);
        manager = new LinearLayoutManager(this);
        myrecyckle.setLayoutManager(manager);

        loadbreakdown();

        final Adater_add adater_add = new Adater_add(addrecycleitems);
        myrecyckle.setAdapter(adater_add);

        // 아이템 터치헬퍼 리사이클러뷰로 입력
        ItemTouchHelper touchHelper = new ItemTouchHelper(new Itemtouchhelpercallback(adater_add));
        touchHelper.attachToRecyclerView(myrecyckle);

        Intent inputintent = getIntent();


        final int input = inputintent.getIntExtra("plusinput",0); // 넘어온 금액입력값
        final TextView puls_add_input = (TextView)findViewById(R.id.get_useraddinput); // plus액티비티에서 전달된 금액을 알려주는 뷰
        String format = comma.format(input); // 숫자 콤마 처리
        puls_add_input.setText(format+"원");

        String cal_input = inputintent.getStringExtra("add_call"); // 넘어온 달력 날짜 값
        final TextView cal_view = (TextView)findViewById(R.id.add_cal_view);
        cal_view.setText(cal_input);

//        SharedPreferences sharedPreferences = getSharedPreferences("add_uri_save",0); // 쉐어 불러오기
//        final String path = sharedPreferences.getString("add_uri",""); // 쉐어 값 스트링으로 담아서
//        Uri myUri = Uri.parse(path); // 스트링 패스값 uri로 저장
//        final ImageView image = (ImageView)findViewById(R.id.imageView3); // 이미지뷰 사용할 것 선언
//        image.setImageURI(myUri); // 이미지 뷰에 셋해준다. uri로 셋

        Button backbtn = (Button) findViewById(R.id.Backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            // 돌아가기 버튼
            @Override
            public void onClick(View view) { // 돌아가기 버튼 클릭시, 메인 액티비티로 전환
                onBackPressed();
            }
        }); // 돌아가기 버튼 종료

        final Button category_add = (Button)findViewById(R.id.category_add_btn); // 내역추가 버튼 선언
        category_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력값을 어레이리스트에 추가해주는 다이얼로그
                AlertDialog.Builder add = new AlertDialog.Builder(AddActivity.this);
                add.setTitle("추가할 내역을 입력하세요");
                final EditText add_edit = new EditText(AddActivity.this);
                add.setView(add_edit);
                add.setPositiveButton("입력완료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 다이얼로그로 입력한 텍스트가 벨류 값으로 어레이리스트에 저장되고 리스트 갱신
                        String value = add_edit.getText().toString();
                        addrecycleitems.add(new Addrecycleitem(value));
                        adater_add.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                add.show();
            }
        }); // 내역추가 버튼 끝


        // 리사이클러뷰가 계속 클릭되는 문제해결을 위해 게스터디텍터로 한 번 클릭만 하게 한다.
        final GestureDetector gestureDetector = new GestureDetector(AddActivity.this,new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });

        myrecyckle.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, final MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e))
                {   // 내역이 셋팅되어 있는 버튼 을 선언한다.
                    // 버튼 값에 텍스트를 가져온다.
                    final CheckBox add_edit =(CheckBox) rv.getChildViewHolder(child).itemView.findViewById(R.id.addlistbtn);
                    Toast.makeText(getApplication(), add_edit.getText().toString(), Toast.LENGTH_SHORT).show();

                    Button mainput = (Button)findViewById(R.id.plus_per_btn); // 입력완료버튼 누르면 메인액티비티로 값 전달
                    mainput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            // 각각 값들을 리스너 안에다 선언해야 넘어간다.
                            int check = 1 ; // 입금에서 넘겼다는 것을 확인시켜주는 변수
                            int a_userinput =input;  // 사용자 금액 입력값 -> 메인 상단으로 보낼 것
                            int s_a_userinput = input; // 금액 입력값 -> 하단 리사이클러뷰로 보낼 것
                            String a_cal_userinput = cal_view.getText().toString(); // 선택 달력날짜 값


                            String a_breakdown_userinput = add_edit.getText().toString(); // 사용자가 선택한 내역 값
                            SharedPreferences share_uri = getSharedPreferences("add_uri_save",0); // 사진이 저장된 쉐어 선언
                            String add_url = share_uri.getString("add_uri",""); // 쉐어 안에 있는 값 넘겨주기 위해 스트링 변수에 넣기

                            StringBuffer stringBuffer = new StringBuffer(a_cal_userinput); // 선택 날짜를 년 월로만 표현하게
                            String time = stringBuffer.substring(0,6); // 뒤에 일자 짜르고 년,월 만 남기기
                            // 10월 ~ 12월 이슈 해결 나중엔 정규표현식으로 하면 좋을 듯
                            String cut = stringBuffer.substring(5,7);
                            String 십월 = time+Integer.toString(0);
                            String 십일월 = time+Integer.toString(1);
                            String 십이월 = time+Integer.toString(2);


                            if (cut.equals(Integer.toString(10)))
                            {
                                intent.putExtra("plusinput",a_userinput);  // 사용자가 입력한 금액을 담는다 -> 상단
                                intent.putExtra("plus_input", s_a_userinput); // 하단
                                intent.putExtra("plus_breakdown_input", a_breakdown_userinput); // 사용자가 선택한 내역 값 담기
                                intent.putExtra("plus_call_input",a_cal_userinput); // 날짜 입력값 담기
                                intent.putExtra("add_check",check); // 입금에서 넘어왔다는 거 알리기 용
                                intent.putExtra("add_url",add_url); // url 스트링으로 넘기기
                                intent.putExtra("add_time",십월); // 현재 년월 값

                                SharedPreferences.Editor editor = share_uri.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                            else if (cut.equals(Integer.toString(11)))
                            {
                                intent.putExtra("plusinput",a_userinput);  // 사용자가 입력한 금액을 담는다 -> 상단
                                intent.putExtra("plus_input", s_a_userinput); // 하단
                                intent.putExtra("plus_breakdown_input", a_breakdown_userinput); // 사용자가 선택한 내역 값 담기
                                intent.putExtra("plus_call_input",a_cal_userinput); // 날짜 입력값 담기
                                intent.putExtra("add_check",check); // 입금에서 넘어왔다는 거 알리기 용
                                intent.putExtra("add_url",add_url); // url 스트링으로 넘기기
                                intent.putExtra("add_time",십일월); // 현재 년월 값

                                SharedPreferences.Editor editor = share_uri.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                            else if (cut.equals(Integer.toString(12)))
                            {
                                intent.putExtra("plusinput",a_userinput);  // 사용자가 입력한 금액을 담는다 -> 상단
                                intent.putExtra("plus_input", s_a_userinput); // 하단
                                intent.putExtra("plus_breakdown_input", a_breakdown_userinput); // 사용자가 선택한 내역 값 담기
                                intent.putExtra("plus_call_input",a_cal_userinput); // 날짜 입력값 담기
                                intent.putExtra("add_check",check); // 입금에서 넘어왔다는 거 알리기 용
                                intent.putExtra("add_url",add_url); // url 스트링으로 넘기기
                                intent.putExtra("add_time",십이월); // 현재 년월 값

                                SharedPreferences.Editor editor = share_uri.edit();
                                editor.clear();
                                editor.apply();

                                startActivity(intent);
                            }
                            else
                            {
                                intent.putExtra("plusinput",a_userinput);  // 사용자가 입력한 금액을 담는다 -> 상단
                                intent.putExtra("plus_input", s_a_userinput); // 하단
                                intent.putExtra("plus_breakdown_input", a_breakdown_userinput); // 사용자가 선택한 내역 값 담기
                                intent.putExtra("plus_call_input",a_cal_userinput); // 날짜 입력값 담기
                                intent.putExtra("add_check",check); // 입금에서 넘어왔다는 거 알리기 용
                                intent.putExtra("add_url",add_url); // url 스트링으로 넘기기
                                intent.putExtra("add_time",time); // 현재 년월 값

                                SharedPreferences.Editor editor = share_uri.edit();
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
            public void onTouchEvent( RecyclerView rv,  MotionEvent e) {            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {            }
        });

        Button picutre = (Button)findViewById(R.id.picture); // 사진찍기 버튼 찾기
        picutre.setOnClickListener(new View.OnClickListener() { // 사진찍기 누르면 이벤트 발생시키는 리스너 만들기
            @Override
            public void onClick(View view) {
                int permissioncheck = ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA); // 사용자에게 권한을 묻는 것 int에 담기
                if(permissioncheck == PackageManager.PERMISSION_DENIED){ //DENIED는 권한이 없을 때
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.CAMERA},0);
                }else { //권한이 있다면 카메라 앱 작동
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }
            }
        });

        Button get_picture = (Button)findViewById(R.id.get_picture); // 사진가져오기 버튼 셋팅
        get_picture.setOnClickListener(new View.OnClickListener() { // 사진가져오기 누르면 이벤트 발생 리스너 만들기
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(AddActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_DENIED){ // 권한이 없다면 동의 묻기
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }else { // 권한이 있을 때
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_PICTURE);
                }
            }
        });

        Log.e(Tag,"oncreate");
    } //oncreate 종료


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData(); // uri 받아오기
                String realpath = getRealpath(selectedImageUri); // uri로 절대경로 찾고 스트링으로 변환
                Log.e("uri 값","uri값은 넘어온다.     "+selectedImageUri);
                ImageView image = (ImageView)findViewById(R.id.imageView3); // 이미지뷰 사용할 것 선언
                image.setImageURI(selectedImageUri); // 이미지 뷰에 셋해준다.
                SharedPreferences share_uri = getSharedPreferences("add_uri_save",0); // 사진이 저장된 쉐어 선언
                SharedPreferences.Editor editor = share_uri.edit();
                editor.putString("add_uri",realpath);
                editor.apply();
                Log.e("절대경로","왜 안넘어올까"+realpath);
            }
        }

    }

    public String getRealpath(Uri uri) { // 사진에서 얻은 uri로 절대 경로 얻기 메소드
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        c.moveToFirst();
        String path = c.getString(index);
        return path;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag,"onpause");
        savebreakdown();

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


//    @Override
//    public void onBackPressed() { //뒤로가기 버튼을 눌렀을 때
//        // 뒤로가기 버튼 누르면
//        // addactivity 정지
//        // mainactivity 띄우기
//        super.onBackPressed();
//        onPause();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//    }
//}

}