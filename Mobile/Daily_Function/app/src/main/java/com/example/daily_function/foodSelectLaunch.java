package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class foodSelectLaunch extends AppCompatActivity {
    ArrayList<food> arrayFoodList;
    Intent intent;
    ArrayList<food> food_list;
    Toolbar myToolbar;
    // 리사이클러뷰에 활용할 것들
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyAdapter2 myAdapter;
    TextView toal_kcal;
    double totalCalcium=0,totalCarbon=0,totalFat=0,totalIron=0,totalFiber=0,totalSugar=0,totalVitaminC=0,totalVitaminB1=0,totalVitaminB2=0,totalKcal=0,totalNa=0,totalProtein=0;
    SimpleDateFormat format;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference PReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_select_launch);
        Toast.makeText(getApplicationContext(), "점심 식사 정보", Toast.LENGTH_SHORT).show();
        intent=getIntent();
        arrayFoodList=(ArrayList<food>)intent.getSerializableExtra("Foodname");
        Serializable s=intent.getSerializableExtra("Foodname");

        toal_kcal=(TextView)findViewById(R.id.tv_total_kcal_launch);

        //툴바 기능
        myToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //리사이클러뷰 초기화
        mRecyclerView = findViewById(R.id.recyclerView3);// 리사이클러뷰 레이아웃 연결
        mRecyclerView.setHasFixedSize(true); //리사이클러뷰 성능강화
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        food_list=new ArrayList<>(); // foodarraylist중 이름,kcal만 담을 리스트


        food_list.clear();
        for(int i=0;i<arrayFoodList.size();i++) {
            Log.d("foodSelect.class", "selection data: " + arrayFoodList.get(i).getFoodname());
            food_list.add(arrayFoodList.get(i));
            Log.d("foodSelect.class", "food_list data: " +food_list.get(i).getFoodname());
            Log.d("foodSelect.class", "food_list data: " +food_list.get(i).getKcal());
            totalKcal+=arrayFoodList.get(i).getKcal();
            totalCalcium+=arrayFoodList.get(i).getCalcicum_mg();
            totalCarbon+=arrayFoodList.get(i).getCarbon_g();
            totalFat+=arrayFoodList.get(i).getFat_g();
            totalFiber+=arrayFoodList.get(i).getTotal_Dietary_Fiber_g();
            totalNa+=arrayFoodList.get(i).getNa_mg();
            totalIron+=arrayFoodList.get(i).getIron_ug();
            totalSugar+=arrayFoodList.get(i).getTotal_sugar_g();
            totalVitaminC+=arrayFoodList.get(i).getVItamin_C_mg();
            totalVitaminB1+=arrayFoodList.get(i).getVitamin_B1_mg();
            totalVitaminB2+=arrayFoodList.get(i).getVitamin_B2_mg();
            totalProtein+=arrayFoodList.get(i).getProtein_g();
        }
        String str_dist=String.format("%.2f",totalKcal);
        toal_kcal.setText("총 칼로리:"+str_dist+"kcal");
        myAdapter = new MyAdapter2(food_list,this);
        mRecyclerView.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();//리스트 저장 및 새로고침
    }

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

                firebaseAuth = FirebaseAuth.getInstance();
                //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
                if(firebaseAuth.getCurrentUser() == null) {
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 가져오기
                String email = user.getEmail(); //현재 유저 이메일(아이디) 가져오기
                int s= email.indexOf("@");
                String email_id=email.substring(0,s);

                //입력데이터 Map으로 변환후 업데이트
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> proValues = null;

                format=new SimpleDateFormat ( "yyyy-MM-dd");
                String format_time = format.format (System.currentTimeMillis());


                //이거 다중선택해하는 경우 있으니 위 oncreate에서 total kcal처럼 노가다해야함.
                firebaseFoodInput post = new firebaseFoodInput(email_id,"점심",format_time,totalCalcium,totalCarbon,totalFat,totalIron,totalFiber,totalSugar,totalVitaminC,totalVitaminB1,totalVitaminB2,totalKcal,totalNa,totalProtein );
                proValues = post.toMap();

                childUpdates.put("/FoodLaunch/" + email_id, proValues);
                PReference.updateChildren(childUpdates);

                Intent intent = new Intent(this, FoodInput.class);
                startActivity(intent);
                return true;

            default: // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}