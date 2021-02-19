package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileInfoActivity extends AppCompatActivity implements View.OnClickListener{ //프로필 정보 보여주는 엑티비티
    private static final String TAG = "ProfileInfoActivity";
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;  //파이어베이스 어쓰

    private FirebaseDatabase pDatabase; //데이터베이스
    private DatabaseReference pReference;
    private ChildEventListener pChild;

    private ListView pListview;
    Button change,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileinfo);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("프로필정보");
        setSupportActionBar(toolbar);

        change =(Button)findViewById(R.id.btn_change);
        change.setOnClickListener(this);

        cancel =(Button)findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //유저의 이메일에서 아이디만 정보빼기
        String email = user.getEmail(); 
        int s= email.indexOf("@");
        String id=email.substring(0,s);

        pListview =(ListView)findViewById(R.id.db_list_view);
        ProfileAdapter pAdapter=new ProfileAdapter(); //사전 정의한 어댑터
        initDatabase();

        pReference=pDatabase.getReference("Client");
        pReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.clear();
                for (DataSnapshot Data : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.


                    Profile pro = Data.getValue(Profile.class);
                    String rid=pro.getId();

                    //System.out.println(rid); //로그 찍기용
                    
                    if(rid.equals(id)) {
                        pAdapter.addItem(pro.getName(), pro.getHeight(), pro.getWeight(), pro.getGender(), pro.getAge());
                    }
                }

                pListview.setAdapter(pAdapter);
                //Adapter.notifyDataSetChanged();
                //listView.setSelection(adapter.getCount()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent profile = new Intent(getApplicationContext(), ProfileChangeActivity.class);
                startActivity(profile);
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                Toast.makeText(this, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }



    private void initDatabase() { //데이터베이스 초기화
        pDatabase = FirebaseDatabase.getInstance();

        pReference = pDatabase.getReference("Client");

        pChild = new ChildEventListener() {

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
        pReference.addChildEventListener(pChild);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pReference.removeEventListener(pChild);
    }

    @Override
    public void onClick(View v) { //버튼 클릭시 이동
        if(v == change) {
            Intent change = new Intent(getApplicationContext(), ProfileChangeActivity.class);
            startActivity(change);
            finish();
        }else if(v == cancel) {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
            finish();
        }
    }
}
