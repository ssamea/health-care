package com.example.daily_function;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import java.util.Calendar;


public class DetailPedometer extends AppCompatActivity {

    Intent pedometerService2;
    BroadcastReceiver receiver;
    String serviceData; //발 걸음
    String serviceData2;// 거리
    String serviceData3;// 칼로리
    TextView countText;
    //gps 변수
    TextView tvDistDif;
    TextView tvKcal;

    //Calendar today = Calendar.getInstance ( );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pedometer);
        countText = (TextView) findViewById(R.id.stepText);
        tvDistDif = (TextView) findViewById(R.id.tv_dist);
        tvKcal=(TextView)findViewById(R.id.tv_kcal);
        pedometerService2 = new Intent(this, StepCheckServices.class);
        receiver = new DetailPedometer.PlayingReceiver();

        try {
            IntentFilter mainFilter = new IntentFilter("com.example.daily_function");
            registerReceiver(receiver, mainFilter);
            //startService(pedometerService);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(pedometerService2);
            }else {
                startService(pedometerService2);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
    class PlayingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("PlayignReceiver", "IN");
            //serviceData = intent.getStringExtra("stepService");
            serviceData = intent.getExtras().getString("stepService");
            countText.setText(serviceData);

            serviceData2 = intent.getExtras().getString("distService");
            tvDistDif.setText("거리: "+serviceData2);

            serviceData3 = intent.getExtras().getString("kcalService");
            tvKcal.setText("칼로리 소모량: "+serviceData3);



        }

    }
}