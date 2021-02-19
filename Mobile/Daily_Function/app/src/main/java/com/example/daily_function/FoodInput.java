package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodInput extends AppCompatActivity {
    Button MorningBtn;
    Button LaunchBtn;
    Button DinnerBtn;
    Button DessertBtn;
    ProgressBar kcalProgressBar; //프로그래스 바
    TextView infoKcal;
    double morningKcal=0,LaunchKcal=0,dinnerKcal=0,dessertKcal=0,dailyKcal=0;
    private FirebaseAuth firebaseAuth;
    //파베 데이터 참조변수
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference1; // 아침 식단 조회용
    private DatabaseReference mReference2; //점심식단
    private DatabaseReference mReference3; //저녁
    private DatabaseReference mReference4; //간식
    private ChildEventListener mChild;
    int integerdailyKcal;
    String TAG="FoodInput.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        infoKcal=(TextView)findViewById(R.id.eat_kcal);
        //프로그래스바
        kcalProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        kcalProgressBar.setMax(10000);


        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail(); //현재 유저 이메일(아이디) 가져오기
        int s= email.indexOf("@");
        String email_id=email.substring(0,s);
        initDatabase(); //아침용
        initDatabase2();//점심
        initDatabase3();//저녁
        initDatabase4();//간식

        mDatabase=FirebaseDatabase.getInstance(); //디비 연결

        //여기 문제임. dataSnapshot.child(email_id)에서 기존 데이터가 없으면 null로 떠버림. 그래서 고쳐봣는데 아예 넘어가버림. 뭐가 이상함
        //아침용
        mReference1=mDatabase.getReference("MorningFood");
        mReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정
                    String check=dataSnapshot.child(email_id).child("id").getValue(String.class);
                   // morningKcal = dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                    if(check==null){
                        morningKcal=0;
                        dailyKcal+=0;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    if(check!=null && check.equals(email_id)){
                        morningKcal = dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                        dailyKcal += morningKcal;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    Log.d(TAG, "morningKcal: " + morningKcal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodInput.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });

        //점심용
        mReference2=mDatabase.getReference("FoodLaunch");
        mReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정.
                   // LaunchKcal = dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                    String check=dataSnapshot.child(email_id).child("id").getValue(String.class);
                    if(check==null){
                        morningKcal=0;
                        dailyKcal+=0;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    if(check!=null && check.equals(email_id)){
                        LaunchKcal = dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                        dailyKcal += LaunchKcal;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    Log.d(TAG, "LaunchKcal: " + LaunchKcal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodInput.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });

        //저녁용
        mReference3=mDatabase.getReference("FoodDinner");
        mReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정
                   // dinnerKcal=dataSnapshot.child(email_id).child("fb_Kcal").getValue(Double.class); //문제발생
                    String check=dataSnapshot.child(email_id).child("fb_client_id").getValue(String.class);
                    if(check==null){
                        morningKcal=0;
                        dailyKcal+=0;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    if(check!=null && check.equals(email_id)){
                        dinnerKcal=dataSnapshot.child(email_id).child("fb_Kcal").getValue(Double.class); //문제발생
                        dailyKcal += dinnerKcal;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    Log.d(TAG, "dinnerKcal: "+dinnerKcal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodInput.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });

        //간식용
        mReference4=mDatabase.getReference("FoodDessert");
        mReference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot myData : dataSnapshot.getChildren()) { ////values에 데이터를 담는 과정
                  //  dessertKcal=dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                    String check=dataSnapshot.child(email_id).child("id").getValue(String.class);
                    if(check==null){
                        dessertKcal=0;
                        dailyKcal+=0;
                        integerdailyKcal=(int)Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal+"kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    if(check!=null && check.equals(email_id)){
                        dessertKcal=dataSnapshot.child(email_id).child("Kcal").getValue(Double.class);
                        dailyKcal += dessertKcal;
                        integerdailyKcal = (int) Math.round(dailyKcal);
                        infoKcal.setText(integerdailyKcal + "kcal");
                        kcalProgressBar.setProgress(integerdailyKcal);
                    }
                    Log.d(TAG, "dessertKcal: "+dessertKcal);
                }
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                Log.d(TAG,"integerdailyKcal:"+integerdailyKcal);
               // intent.putExtra("totalKcal",integerdailyKcal);
                intent.putExtra("totalKcal",integerdailyKcal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodInput.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }

        });


        int integerdailyKcal=(int)Math.round(dailyKcal);
        Log.d(TAG, "integerdailyKcal: "+integerdailyKcal);




        // 아침,점심,저녁, 간식 버튼이벤트 리스너들
        MorningBtn= (Button) findViewById(R.id.morning_btn);
        MorningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), Search.class);
                startActivity(intent);
            }
        });

        LaunchBtn= (Button) findViewById(R.id.launch_btn);
        LaunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), LaunchSearch.class);
                startActivity(intent);
            }
        });

        DinnerBtn= (Button) findViewById(R.id.dinner_btn);
        DinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), DinnerSearch.class);
                startActivity(intent);
            }
        });

        DessertBtn= (Button) findViewById(R.id.dessert_btn);
        DessertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), DessertSearch.class);
                startActivity(intent);
            }
        });

    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference1 = mDatabase.getReference("MorningFood");

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
        mReference1.addChildEventListener(mChild);
    }

    private void initDatabase2() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference2 = mDatabase.getReference("FoodLaunch");

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
        mReference2.addChildEventListener(mChild);
    }

    private void initDatabase3() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference3 = mDatabase.getReference("FoodDinner");

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
        mReference3.addChildEventListener(mChild);
    }

    private void initDatabase4() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference4 = mDatabase.getReference("FoodDessert");

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
        mReference4.addChildEventListener(mChild);
    }

}