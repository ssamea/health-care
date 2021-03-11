package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Board_popularActivity extends AppCompatActivity {

    Button search;
    Button insert;
    EditText search_edit;

    int boardnum;

    String filter;

    private FirebaseAuth firebaseAuth;  //파이어베이스 어쓰

    private FirebaseDatabase bDatabase; //데이터베이스
    private DatabaseReference bReference;
    private ChildEventListener bChild;

    private ListView bListview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_popular);

        search_edit=(EditText)findViewById(R.id.search_edit);

        bListview=(ListView)findViewById(R.id.board_list_view);
        BoardDataAdapter bAdapter = new BoardDataAdapter();
        bListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String bo_no = ((BoardData)bAdapter.getItem(position)).getBoard_No();
                String bo_title = ((BoardData)bAdapter.getItem(position)).getTitle();
                String bo_content = ((BoardData)bAdapter.getItem(position)).getContent();
                String bo_date = ((BoardData)bAdapter.getItem(position)).getDate();
                int bo_hits= ((BoardData)bAdapter.getItem(position)).getHits();
                int bo_get=((BoardData)bAdapter.getItem(position)).getGet();
                String bo_id=((BoardData)bAdapter.getItem(position)).getId();
                String bo_download=((BoardData)bAdapter.getItem(position)).getDownload();

                BoardData board= new BoardData(bo_no,bo_title,bo_content,bo_date,bo_hits,bo_get,bo_id,bo_download);

                Intent detail = new Intent(getApplicationContext(), BoardDetailActivity.class);
                detail.putExtra("board",board);
                startActivity(detail);

            }

        });

        initDatabase();

        Spinner spinner =(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bReference=bDatabase.getReference("Board").child("BoardData");
        bReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.clear();
                for (DataSnapshot Data : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.

                    BoardData board = Data.getValue(BoardData.class);

                    bAdapter.addItem(board.getBoard_No(), board.getTitle(),board.getContent(),board.getDate(),board.getHits(), board.getGet(), board.getId());


                }


                bListview.setAdapter(bAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search=(Button)findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_text=search_edit.getText().toString();



            }
        });

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
