package com.example.daily_function;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StepCheckServices extends Service implements SensorEventListener {
    public StepCheckServices() {
    }
    int count = StepValue.Step;
    // 맴버변수 (마지막과 현재값을 비교하여 변위를 계산하는 방식
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    public static String CHANNEL_ID;

    private FirebaseAuth firebaseAuth;
    //파베 데이터 참조변수
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference; // 아침 식단 조회용
    private ChildEventListener mChild;
    double weight;
    int cal;
    String sex;
    float distance;
    String str_dist;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference PReference = firebaseDatabase.getReference();

    //날짜 비교를 위한 변수들
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String today=null;

    String dt = "2021-01-26"; //오늘 날짜와 비교하기 위한 변수
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "IN");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("") // 제목 설정
                    .setContentText("").build(); // 내용 설정



            startForeground(1, notification);

            // 시스템 서비스에서 센서메니져 획득
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            // TYPE_ACCELEROMETER의 기본 센서객체를 획득
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("onStartCommand", "IN");
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        return START_STICKY; //서비스가 종료되어도 자동으로 다시 실행
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "IN");
        //  if (sensorManager != null) {
        //    sensorManager.unregisterListener(this);
        //  StepValue.Step = 0;
        //  }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && sensorManager != null) {
            sensorManager.unregisterListener(this);
            StepValue.Step = 0;
        }

    }

    // 센서에 변화를 감지하기 위해 계속 호출되고 있는 함수
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("onSensorChanged", "IN");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            PReference = firebaseDatabase.getInstance().getReference();

            if (gabOfTime > 100) { //  gap of time of step count
                Log.i("onSensorChanged_IF", "FIRST_IF_IN");
                lastTime = currentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];


                //  변위의 절대값  / gabOfTime * 10000 하여 스피드 계산
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                // 임계값보다 크게 움직였을 경우 다음을 수행
                if (speed > SHAKE_THRESHOLD) {
                    Log.i("onSensorChanged_IF", "SECOND_IF_IN");
                    //Intent myFilteredResponse = new Intent("com.example.daily_function"); //sendBroad()로 뺌

                    //아래 if문으로 옮김
                    //StepValue.Step = count++;
                    //Notification_info();

                    //여기에서 날짜를 계산 해야할 듯.
                    today=dateFormat.format(new Date()); //오늘 날짜 가져오기

                    if(dt.equals(today)){ // today와 비교할 날짜가 같다면
                        StepValue.Step = count++; //발걸음 증가
                        Notification_info();

                        sendBroad(StepValue.Step);

                    }

                    /* sendBroad()로 뺌
                    String msg = StepValue.Step / 2 + ""; //발걸음 메인액티비티 결과값 파싱
                    String dist_msg = getDistanceRun(StepValue.Step/2) + "km"; // 이동거리
                    String kcal_msg = getCalorie(StepValue.Step)+"kcal"; //칼로리
                    String prog_msg= StepValue.Step/2+"";

                    // 발걸음과, 이동거리 2개다 넣기
                    myFilteredResponse.putExtra("stepService", msg);
                    myFilteredResponse.putExtra("kcalService", kcal_msg);
                    myFilteredResponse.putExtra("distService", dist_msg);
                    myFilteredResponse.putExtra("progService", prog_msg);

                    // 발걸음 브로드 캐스트
                    sendBroadcast(myFilteredResponse);

                     */
                    else { //같지 않다면
                        c = Calendar.getInstance();
                        StepValue.Step=0;
                        count=0;
                        Notification_info();
                        sendBroad(StepValue.Step);
                        try {
                            c.setTime(dateFormat2.parse(dt));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        c.add(Calendar.DATE, 1);  // number of days to add
                        dt = dateFormat2.format(c.getTime());  // dt is now the new date
                    }

                }

                // 마지막 위치 저장
                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendBroad(int steps){
        Intent myFilteredResponse = new Intent("com.example.daily_function");
        String msg = steps / 2 + ""; //발걸음 메인액티비티 결과값 파싱
        String dist_msg = getDistanceRun(steps/2) + "km"; // 이동거리
        String kcal_msg = getCalorie(steps)+"kcal"; //칼로리
        String prog_msg= steps/2+"";

        // 발걸음과, 이동거리 2개다 넣기
        myFilteredResponse.putExtra("stepService", msg);
        myFilteredResponse.putExtra("kcalService", kcal_msg);
        myFilteredResponse.putExtra("distService", dist_msg);
        myFilteredResponse.putExtra("progService", prog_msg);

        // 발걸음 브로드 캐스트
        sendBroadcast(myFilteredResponse);

    }

    //발걸음 거리 계산 함수
    public String getDistanceRun(int steps){
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail(); //현재 유저 이메일(아이디) 가져오기
        int s= email.indexOf("@");
        String email_id=email.substring(0,s);


        mDatabase=FirebaseDatabase.getInstance(); //디비 연결

        initDatabase();

        //성별 가져오기
        mReference=mDatabase.getReference("Client");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정
                    sex=dataSnapshot.child(email_id).child("gender").getValue(String.class);
                    Log.d("Step.java","sex:"+sex);

                }
                if (sex.equals("남")){
                    distance = (float)(steps*78)/(float)100000.0;  //남성은 78cm,  여성은 70cm 이거 그래서 나중에 디비에서 성별 받아와서 추가 수정해야함
                    str_dist=String.format("%.2f",distance);
                }

                else if(sex.equals("여")){
                    distance = (float)(steps*70)/(float)100000.0;  //남성은 78cm,  여성은 70cm 이거 그래서 나중에 디비에서 성별 받아와서 추가 수정해야함
                    str_dist=String.format("%.2f",distance);
                }

                else {
                    Toast.makeText(StepCheckServices.this,"성별을 입력해주세요!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),ProfileChangeActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StepCheckServices.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });
      //  distance = (float)(steps*78)/(float)100000.0;  //남성은 78cm,  여성은 70cm 이거 그래서 나중에 디비에서 성별 받아와서 추가 수정해야함
       // String str_dist=String.format("%.2f",distance);
        return str_dist;
    }

    //칼로리 계산 함수
    public int getCalorie(int steps){
        //double weight;
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail(); //현재 유저 이메일(아이디) 가져오기
        int s= email.indexOf("@");
        String email_id=email.substring(0,s);


        mDatabase=FirebaseDatabase.getInstance(); //디비 연결

        initDatabase();

        //몸무게 가져오기
        mReference=mDatabase.getReference("Client");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정
                    weight=dataSnapshot.child(email_id).child("weight").getValue(Double.class);
                    Log.d("Step.java","weight:"+weight);

                    if(weight==0){
                        Toast.makeText(StepCheckServices.this,"몸무게를 입력해주세요!",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),ProfileChangeActivity.class);
                        startActivity(intent);
                    }

                }
                cal = (int)Math.floor((steps/10000.0)*weight*5.5); //62.4가 weight
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StepCheckServices.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });




        return (cal/2);
    }

    public void Notification_info() {


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("notificationId", count); //전달할 값
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground)) //BitMap 이미지 요구
                .setContentTitle("오늘의 걸음: "+StepValue.Step/2)
                .setContentText("10000걸음까지 파이팅입니다!")
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 메인액티비로 이동하도록 설정
                .setAutoCancel(true);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴

    }
    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Client");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

}