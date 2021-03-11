package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
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

public class Board_totalActivity extends AppCompatActivity {

    private static final String TAG = "Board" ;
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


    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크

    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_total);

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

        bListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                }
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

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;

                if (filterText.length() > 0) {
                    bListview.setFilterText(filterText) ;
                } else {
                    bListview.clearTextFilter() ;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

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
