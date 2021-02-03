package com.example.daily_function;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailFoodInfo extends AppCompatActivity {
    ArrayList<food> arrayFoodInfo;
    Intent intent;
    TextView foodName,info_company,ServingSize,kcal,na,protein, Calcicum,Fat,Iron, Total_Dietary_Fiber,Total_sugar, VitaminC, VitaminB1, VitaminB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food_info);

        foodName=(TextView)findViewById(R.id.foodName);
        info_company=(TextView)findViewById(R.id.info_company);
        ServingSize=(TextView)findViewById(R.id.ServingSize);
        kcal=(TextView)findViewById(R.id.kcal);
        na=(TextView)findViewById(R.id.na);
        protein=(TextView)findViewById(R.id.protein);
        Calcicum=(TextView)findViewById(R.id.Calcicum);
        Fat=(TextView)findViewById(R.id.Fat);
        Iron=(TextView)findViewById(R.id.Iron);
        Total_Dietary_Fiber=(TextView)findViewById(R.id.Total_Dietary_Fiber);
        Total_sugar=(TextView)findViewById(R.id.Total_sugar);
        VitaminC=(TextView)findViewById(R.id.VitaminC);
        VitaminB1=(TextView)findViewById(R.id.VitaminB1);
        VitaminB2=(TextView)findViewById(R.id.VitaminB2);

        intent=getIntent();
        arrayFoodInfo=(ArrayList<food>)intent.getSerializableExtra("foodInfo");
        Serializable s=intent.getSerializableExtra("foodInfo");

        for(int i=0;i<arrayFoodInfo.size();i++) {
            Log.d("foodSelect.class", "selection data: " + arrayFoodInfo.get(i));
        }

        foodName.setText(arrayFoodInfo.get(0).getFoodname());
        info_company.setText(arrayFoodInfo.get(0).getCompany());
        ServingSize.setText(arrayFoodInfo.get(0).getServingSize()+"g");
        kcal.setText(arrayFoodInfo.get(0).getKcal()+"kcal");
        na.setText(arrayFoodInfo.get(0).getNa_mg()+"mg");
        protein.setText(arrayFoodInfo.get(0).getProtein_g()+"g");
        Calcicum.setText(arrayFoodInfo.get(0).getCalcicum_mg()+"mg");
        Fat.setText(arrayFoodInfo.get(0).getFat_g()+"g");
        Iron.setText(arrayFoodInfo.get(0).getIron_ug()+"ug");
        Total_Dietary_Fiber.setText(arrayFoodInfo.get(0).getTotal_Dietary_Fiber_g()+"g");
        Total_sugar.setText(arrayFoodInfo.get(0).getTotal_sugar_g()+"g");
        VitaminC.setText(arrayFoodInfo.get(0).getVItamin_C_mg()+"mg");
        VitaminB1.setText(arrayFoodInfo.get(0).getVitamin_B1_mg()+"mg");
        VitaminB2.setText(arrayFoodInfo.get(0).getVitamin_B2_mg()+"mg");


    }
}