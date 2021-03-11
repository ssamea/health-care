package com.example.daily_function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;  //파이어베이스 어쓰

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference bReference = firebaseDatabase.getReference();

    private FirebaseDatabase cDatabase; //데이터베이스
    private DatabaseReference cReference;
    private ChildEventListener cChild;
    private ListView cListview;
    private Context context = this;

    TextView board_date;
    TextView board_title;
    TextView board_content;

    EditText board_comment;

    Button comment_register;

    Button change,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        Intent intent = getIntent();
        BoardData bd = (BoardData)intent.getSerializableExtra("board");

        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        bReference = firebaseDatabase.getInstance().getReference();

        board_title=(TextView)findViewById(R.id.title_tv);
        board_date=(TextView)findViewById(R.id.date_tv);
        board_content=(TextView)findViewById(R.id.content_tv);

        board_title.setText(bd.getTitle());
        board_date.setText(bd.getDate());
        board_content.setText(bd.getContent());

        board_comment=(EditText)findViewById(R.id.comment_et);

        change=(Button)findViewById(R.id.change_button);

        delete=(Button)findViewById(R.id.del_button);

        BoardData cbd = new BoardData(bd.getBoard_No(),bd.getTitle(),bd.getContent(),bd.getDate(),bd.getHits()+1,bd.getGet(),bd.getId(),bd.getDownload()); //조회수 증가 하기 위함

        FirebaseUser cuser = firebaseAuth.getCurrentUser();

        String cemail = cuser.getEmail();
        int cs= cemail.indexOf("@");
        String cid=cemail.substring(0,cs);

        if(bd.getId().equals(cid)){
            change.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> boardValues = null;

        boardValues = cbd.toMap();

        childUpdates.put("/Board/BoardData/"+cbd.getBoard_No(),boardValues);
        bReference.updateChildren(childUpdates);

        comment_register =(Button)findViewById(R.id.reg_button);
        comment_register.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                String comment=board_comment.getText().toString();

                Calendar cal = Calendar.getInstance();
                Date nowDate = cal.getTime();
                SimpleDateFormat dataformat = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");

                //유저의 이메일에서 아이디만 정보빼기

                FirebaseUser user = firebaseAuth.getCurrentUser();

                String email = user.getEmail();
                int s= email.indexOf("@");
                String id=email.substring(0,s);

                String c_date = dataformat.format(nowDate);

                String c_num=cReference.child("Board").child("CommentData").child(cbd.getBoard_No()).push().getKey();

                if(TextUtils.isEmpty(comment)){
                    return;
                }

                Comment cm = new Comment(c_num,id,c_date,comment);
                cReference.child(c_num).setValue(cm);

                startActivity(new Intent(getApplicationContext(), Board_totalActivity.class));
                Toast.makeText(context, "댓글이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();

            }

        });

        cListview =(ListView)findViewById(R.id.cm_list_view);
        CommentAdapter cAdapter=new CommentAdapter(); //사전 정의한 어댑터
        initDatabase();

        cReference=cDatabase.getReference("Board").child("CommentData").child(cbd.getBoard_No());
        cReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //adapter.clear();
                for (DataSnapshot Data : dataSnapshot.getChildren()) {
                    // child 내에 있는 데이터만큼 반복합니다.

                    Comment cmt =Data.getValue(Comment.class);

                        cAdapter.addItem(cmt.getcId(), cmt.getcDate(),cmt.getcContent());

                }

                cListview.setAdapter(cAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent board =new Intent(getApplicationContext(),Board_totalActivity.class);
        startActivity(board);
        finish();
    }

    private void initDatabase() { //데이터베이스 초기화
        cDatabase = FirebaseDatabase.getInstance();

        cReference = cDatabase.getReference();

        cChild = new ChildEventListener() {

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
        cReference.addChildEventListener(cChild);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cReference.removeEventListener(cChild);
    }
}
