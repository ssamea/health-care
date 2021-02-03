package com.example.daily_function;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ProfileChangeActivity extends AppCompatActivity { //프로필 변경 액티비티

    EditText NAME,HEIGHT,WEIGHT,AGE;
    RadioButton GENDER;
    String Tname,Tgender;
    Double Theight,Tweight;
    int Tage;

    private RadioGroup rg;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference PReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilechange);

        rg = (RadioGroup)findViewById(R.id.radioGroup1);

        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        NAME = (EditText) findViewById(R.id.name);
        WEIGHT = (EditText) findViewById(R.id.weight);
        HEIGHT = (EditText) findViewById(R.id.height);
        AGE = (EditText) findViewById(R.id.age);
        GENDER=(RadioButton)findViewById(rg.getCheckedRadioButtonId());

        ImageButton change = (ImageButton) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tname = NAME.getText().toString();
                Theight = Double.parseDouble(HEIGHT.getText().toString());
                Tweight = Double.parseDouble(WEIGHT.getText().toString());//HEIGHT
                Tage =Integer.parseInt(AGE.getText().toString());
                Tgender=GENDER.getText().toString();


                if (Tname.length()<2) {
                    Toast.makeText(getApplicationContext(), "이름을 정확하게 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                } else if (Tage>100) {
                    Toast.makeText(getApplicationContext(), "나이를 정확히 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    PReference = firebaseDatabase.getInstance().getReference();
                    
                    String email = user.getEmail();
                    int s= email.indexOf("@");
                    String email_id=email.substring(0,s);

                    //입력데이터 Map으로 변환후 업데이트
                    Map<String, Object> childUpdates = new HashMap<>();
                    Map<String, Object> proValues = null;

                    Profile post = new Profile(email_id, Tname,Theight,Tweight, Tgender, Tage);
                    proValues = post.toMap();

                    childUpdates.put("/Client/" + email_id, proValues);
                    PReference.updateChildren(childUpdates);

                    Intent menu = new Intent(getApplication(), ProfileInfoActivity.class);

                    startActivity(menu);
                    finish();
                    Toast.makeText(getApplication(), Tname + "님께서 프로필 수정을 완료하였습니다..", Toast.LENGTH_SHORT).show();

                }
            } });
        ImageButton cancel = (ImageButton) findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(getApplication(), ProfileInfoActivity.class);
                startActivity(cancel);
                finish();
            }
        });
    }

}