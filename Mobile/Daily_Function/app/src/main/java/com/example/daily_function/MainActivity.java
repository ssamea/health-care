package com.example.daily_function;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity{

    Intent pedometerService;
    BroadcastReceiver receiver;
    private FirebaseAuth firebaseAuth;

    boolean flag = true;
    String serviceData; //발 걸음
    String serviceData4;// 발걸음을 프로그래스 바로 나타내기 위한 변수

    TextView countText;
    Button FoodBtn;
    Button AiRecoBtn; //Ai 식단 추천
    Button DisBtn; // 질병 정보
    Button AiExecBtn; // Ai 운동
    Button BoardBtn; //게시판
    Button ProfileBtn; //프로필

    ProgressBar step_progressBar; //발걸음 프로그래스 바
    ProgressBar kcal_progressBar;
    int progress;
    int progress2;
    TextView kcal;

    //카메라를 위한 변수들
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이 것이 필요하다(미디어 스캐닝)



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedometerService = new Intent(this, StepCheckServices.class);
        receiver = new PlayingReceiver();

        countText = (TextView) findViewById(R.id.stepText);
        step_progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        step_progressBar.setMax(10000);
        progress =0;

        kcal_progressBar =(ProgressBar)findViewById(R.id.progressBar2);
        kcal_progressBar.setMax(2200);
        progress2=0;
        kcal=(TextView)findViewById(R.id.eat_kcal);


        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // playingBtn = (Button) findViewById(R.id.btnStopService);
        /*
        playingBtn.setOnClickListener(new View.OnClickListener() {

           @Override
            public void onClick(View v) {

                if (flag) {
                    // TODO Auto-generated method stub
                    try {
                IntentFilter mainFilter = new IntentFilter("com.example.daily_functions");
                registerReceiver(receiver, mainFilter);
                //startService(pedometerService);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(pedometerService);
                }else {
                    startService(pedometerService);
                }

            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
                } else {

                    playingBtn.setText("Go !!");

                    // TODO Auto-generated method stub
                    try {

                        unregisterReceiver(receiver);

                        stopService(manboService);

                        // txtMsg.setText("After stoping Service:\n"+service.getClassName());
                    } catch (Exception e) {
                        // TODO: handle exception
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }

                flag = !flag;

            }
        });
        */

        //if (flag) {
        try {
            IntentFilter mainFilter = new IntentFilter("com.example.daily_function");
            registerReceiver(receiver, mainFilter);
            //startService(pedometerService);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(pedometerService);
            }else {
                startService(pedometerService);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        //   }
        /* 여기는 이제 다음날 바꿀경우 0으로 초기화하는 구간
        else {


            // TODO Auto-generated method stub
            try {

                unregisterReceiver(receiver);

                stopService(pedometerService);

                // txtMsg.setText("After stoping Service:\n"+service.getClassName());
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }


        }
        */
        //   flag = !flag;

        ProfileBtn=(Button)findViewById(R.id.ProfileMv);
        ProfileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        //원형 레이아웃 클릭시 상세정보 액티비티 이동
        ViewGroup layout = (ViewGroup) findViewById(R.id.re2);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(v.getContext(), DetailPedometer.class);
                startActivity(intent);
            }
        });


        // 음식 입력 액티비티 이동
        Intent kcalintent=getIntent();
        int total_kcal=kcalintent.getIntExtra("totalKcal",0);
        progress2=total_kcal;
        kcal_progressBar.setProgress(progress2);
        kcal.setText(total_kcal+"kcal");

        FoodBtn= (Button) findViewById(R.id.input_food);
        FoodBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), FoodInput.class);
                startActivity(intent);
            }
        });

        AiRecoBtn=(Button)findViewById(R.id.AiFoodMv);
        AiRecoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), AiRecomand.class);
                startActivity(intent);
            }
        });

        DisBtn=(Button)findViewById(R.id.DiseaseMv);
        DisBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), DiseaseInfo.class);
                startActivity(intent);
            }
        });

        //Ai트레이너
        AiExecBtn=(Button)findViewById(R.id.AiExecMv);
        AiExecBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), AiExercise.class);
                startActivity(intent);
            }
        });

        BoardBtn=(Button)findViewById(R.id.BoardMv);
        BoardBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), Board.class);
                startActivity(intent);
            }
        });




    }


    // 하루 지나면 발걸음수를 디비에 저장하고 0으로 초기화 추가해야함
    class PlayingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PlayignReceiver", "IN");
            //발걸음수
            serviceData = intent.getExtras().getString("stepService");
            countText.setText(serviceData);

            //프로그래스바
            serviceData4=intent.getExtras().getString("progService");
            progress=Integer.parseInt(serviceData4);
            if (progress==10000) {
                progress=0;
                step_progressBar.setProgress(0);
            }
            else
                step_progressBar.setProgress(progress);

        }

    }

}