package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board_mytitleActivity extends AppCompatActivity {

    private static final String TAG = "Board" ;
    
    Button insert;
    EditText search_edit;

    private FirebaseAuth firebaseAuth;  //파이어베이스 어쓰


    RecyclerBoardAdapter badapter;
    private RecyclerView recyclerView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_mytitle);

        search_edit = (EditText) findViewById(R.id.search_edit);

        recyclerView = findViewById(R.id.recyclerView);
        //searchView=findViewById(R.id.searchView);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        insert=(Button)findViewById(R.id.btn_insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(getApplicationContext(), BoardInsertActivity.class);
                startActivity(insert);
                finish();

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }



        FirebaseUser user = firebaseAuth.getCurrentUser();

        user = firebaseAuth.getCurrentUser();

        String r_email = user.getEmail();
        int s= r_email.indexOf("@");
        String r_id=r_email.substring(0,s);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //본인 아이디에 맞는 내용만 찾기
        FirebaseRecyclerOptions<BoardData> options=
                new FirebaseRecyclerOptions.Builder<BoardData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Board").child("BoardData").orderByChild("id").equalTo(r_id),BoardData.class)
                        .build();

        badapter=new RecyclerBoardAdapter(options);
        recyclerView.setAdapter(badapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        badapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        badapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchboard,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();
        //SearchView searchView=findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        user = firebaseAuth.getCurrentUser();

        String r_email = user.getEmail();
        int rs= r_email.indexOf("@");
        String r_id=r_email.substring(0,rs);

        FirebaseRecyclerOptions<BoardData> options=
                new FirebaseRecyclerOptions.Builder<BoardData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Board").child("BoardData").orderByChild("id").equalTo(r_id).startAt(s).endAt(s+"\uf8ff"),BoardData.class)
                        .build();

        badapter=new RecyclerBoardAdapter(options);
        badapter.startListening();
        recyclerView.setAdapter(badapter);
    }
}
