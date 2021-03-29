package com.example.daily_function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BoardActivity extends AppCompatActivity {

    private static final String TAG = "Board";
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;  //파이어베이스 어쓰
    private DrawerLayout bDrawerLayout; //레이아웃 설정
    private Context context = this;
    private ListView bListview;

    private FirebaseDatabase bDatabase; //데이터베이스
    private DatabaseReference bReference;
    private ChildEventListener bChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        Spinner spinner =(Spinner)findViewById(R.id.spinner);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("커뮤니티 게시판");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); //뒤로가기 버튼 이미지 지정

        bDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.board_nav_view);

        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        bListview =(ListView)findViewById(R.id.board_list_view);
        Board_infoAdapter bAdapter=new Board_infoAdapter(); //사전 정의한 어댑터

        bListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((Board_info)bAdapter.getItem(position)).getMenu();

                if(title.equals("Notice(공지사항)")){
                    Toast.makeText(context, title + ": 공지사항 보기", Toast.LENGTH_SHORT).show();
                    Intent notice = new Intent(getApplication(), Board_noticeActivity.class);
                    startActivity(notice);
                    finish();

                }else if(title.equals("전체 글보기")){
                    Toast.makeText(context, title + ": 전체 글보기", Toast.LENGTH_SHORT).show();
                    Intent total = new Intent(getApplication(), Board_totalActivity.class);
                    startActivity(total);
                    finish();
                }
                else if(title.equals("인기 글보기")){
                    Toast.makeText(context, title + ": 인기 글보기", Toast.LENGTH_SHORT).show();
                    Intent popular = new Intent(getApplication(), Board_popularActivity.class);
                    startActivity(popular);
                    finish();
                }
                else if(title.equals("내가 쓴 글보기")){
                    Toast.makeText(context, title + ": 내가 쓴 글 보기", Toast.LENGTH_SHORT).show();
                    Intent mytitle = new Intent(getApplication(), Board_mytitleActivity.class);
                    startActivity(mytitle);
                    finish();
                }else{
                    Toast.makeText(context, "원하시는 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        initDatabase();

        bReference=bDatabase.getReference("Board").child("Board_Menu");
        bReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.clear();
                for (DataSnapshot Data : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.

                    String board_menu = Data.getValue(String.class);

                    bAdapter.addItem(board_menu);
                }

                bListview.setAdapter(bAdapter);
                //Adapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                bDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.profile){
                    Toast.makeText(context, title + ": 프로필 보기", Toast.LENGTH_SHORT).show();
                    Intent profile = new Intent(BoardActivity.this, ProfileInfoActivity.class);
                    startActivity(profile);
                    finish();
                }
                else if(id == R.id.pedometer){
                    Toast.makeText(context, title + ": 만보기 보기", Toast.LENGTH_SHORT).show();
                    Intent pedometer = new Intent(BoardActivity.this, DetailPedometer.class);
                    startActivity(pedometer);
                    finish();
                }
                else if(id == R.id.diet){
                    Toast.makeText(context, title + ": 식단 보기", Toast.LENGTH_SHORT).show();
                    Intent foodinput = new Intent(BoardActivity.this, FoodInput.class);
                    startActivity(foodinput);
                    finish();
                }else if(id == R.id.ai_recommend){
                    Toast.makeText(context, title + ": AI 추천 식단", Toast.LENGTH_SHORT).show();
                    Intent ai_food = new Intent(BoardActivity.this, AiRecomand.class);
                    startActivity(ai_food);
                    finish();
                }else if(id == R.id.disease){
                    Toast.makeText(context, title + ": 질병 정보", Toast.LENGTH_SHORT).show();
                    Intent disease = new Intent(BoardActivity.this, DiseaseInfoActivity.class);
                    startActivity(disease);
                    finish();
                }else if(id == R.id.ai_trainer){
                    Toast.makeText(context, title + ": AI 트레이너", Toast.LENGTH_SHORT).show();
                    Intent ai_exercise = new Intent(BoardActivity.this, AiExercise.class);
                    startActivity(ai_exercise);
                    finish();
                }else if(id == R.id.board){
                    Toast.makeText(context, title + ": 게시판 메인", Toast.LENGTH_SHORT).show();
                    Intent board = new Intent(BoardActivity.this, BoardActivity.class);
                    startActivity(board);
                    finish();
                }

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent board =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(board);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                bDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void initDatabase() { //데이터베이스 초기화
        bDatabase = FirebaseDatabase.getInstance();

        bReference = bDatabase.getReference("Board");

        bChild = new ChildEventListener() {

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
        bReference.addChildEventListener(bChild);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bReference.removeEventListener(bChild);
    }
}