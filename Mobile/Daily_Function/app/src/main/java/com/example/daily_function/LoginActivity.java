package com.example.daily_function;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailText; //이메일 입력
    EditText passText; //패스워드 입력

    TextView textviewMessage;
    
    TextView register; //회원가입
    TextView findpass; //비밀번호 찾기

    Button buttonlogin; //회원가입

    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth; //파이어베이스AUTH



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth =  FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 메인 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        emailText = (EditText) findViewById(R.id.email);
        passText = (EditText) findViewById(R.id.pass);
        buttonlogin =(Button)findViewById(R.id.buttonlogin);
        register=(TextView)findViewById(R.id.register);
        findpass=(TextView)findViewById(R.id.findpass);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);

        progressDialog = new ProgressDialog(this);

        buttonlogin.setOnClickListener(this);
        register.setOnClickListener(this);
        findpass.setOnClickListener(this);

    }

    public void login() {
        String Email = emailText.getText().toString().trim();
        String Pass = passText.getText().toString().trim();

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Pass)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("로그인 실패 유형\n - Email와 papssword를 확인해주세요!!\n ");
                        }
                    }
                });

    }


    public void register(View view){
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);

    }

    @Override
    public void onClick(View v) {
        if(v == buttonlogin) {
            login();
        }

        if(v == register) {
            finish();
            //  회원가입
            Intent intent1;
            intent1 = new Intent(this, RegisterActivity.class);
            startActivity(intent1);
            //  finish();
        }

        if(v == findpass) {
            finish();
            //아이디찾기
            Intent intent2;
            intent2 = new Intent(this, FindPassActivity.class);
            startActivity(intent2);
            //  finish();
        }
    }

}