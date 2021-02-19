package com.example.daily_function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DinnerSearch extends AppCompatActivity {

    EditText ed_search;
    Button btn;
    String str_search;

    // 리사이클러뷰에 활용할 것들
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<food> arrayList;
    MyAdapter myAdapter;


    //파이어 스토어 참조 변수
    FirebaseFirestore db;
    DocumentReference docRef;
    static final String TAG="Search_Activity_Dinner";

    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_search);
        Toast.makeText(getApplicationContext(), "저녁 식사 입력", Toast.LENGTH_SHORT).show();

        // firestor 객체 얻어오기
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("foodinfo").document("0e6383e0-533f-11eb-b41e-1377c1a09322");

        //위젯 연결
        ed_search = (EditText) findViewById(R.id.foodName);
        btn = (Button) findViewById(R.id.button_search);
        //str_search=ed_search.getText().toString();// 에디트텍스트 스트링값으로 변환

        //리사이클러뷰 초기화
        mRecyclerView = findViewById(R.id.recyclerView);// 리사이클러뷰 레이아웃 연결
        mRecyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        arrayList=new ArrayList<>();//food 객체를 담을 배열리스트(어댑터쪽으로)

        myToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 검색을 클릭하면 editText의 값을 통해 디비를 조회하는 기능
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                str_search=ed_search.getText().toString();// 에디트텍스트 스트링값으로 변환
                //파베 스토어 데이터가져오기
                db.collection("foodinfo")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                            @Override
                            public void onSuccess(QuerySnapshot  documentSnapshots) {

                                if (documentSnapshots.isEmpty()) {
                                    Log.d(TAG, "onSuccess: LIST EMPTY");
                                    return;
                                }
                                else {
                                    arrayList.clear();

                                    for (DocumentSnapshot document: documentSnapshots){
                                        if(document.getString("Foodname").contains(str_search)){
                                            Log.d(TAG,"getname:"+document.getString("Foodname"));

                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            food foodinfo = document.toObject(food.class);// 만들어뒀던 food 클래스의 객체 변수에 데이터 담기
                                            arrayList.add(foodinfo); //리스트에 파이어스토어 데이터담고 리사이클러뷰에 보낼 준비.
                                            Log.d(TAG, "DocumentSnapshot arraylist: " +arrayList);
                                        }

                                        // else{
                                        //  Toast.makeText(getApplicationContext(), "음식을 입력해주세요", Toast.LENGTH_LONG).show();
                                        //  }
                                    }


                                } //else

                                myAdapter.notifyDataSetChanged();//리스트 저장 및 새로고침
                                ed_search.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                            }
                        });


            }//onclick



        }); // btn

        myAdapter = new MyAdapter(arrayList,this);
        mRecyclerView.setAdapter(myAdapter);


    }//oncreate
    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;

            case R.id.toolbar_next_button:  // 오른쪽 상단 버튼 눌렀을 때
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, foodSelectDinner.class);
                ArrayList<food> selection_food=new ArrayList<>();

                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).isSelected()) {
                        selection_food.add(arrayList.get(i));
                    }
                }
                intent.putExtra("Foodname",selection_food);
                for(int i=0;i<selection_food.size();i++) {
                    Log.d(TAG, "selection data: " + selection_food.get(i));
                }
                startActivity(intent);
                return true;

            default: // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}