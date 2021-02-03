package com.example.daily_function;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> implements Serializable {
    private ArrayList<food> arrayList;
    private Context context;

    public MyAdapter(ArrayList<food> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    /*
    public MyAdapter(String str_search) {
    }
    */

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //아이템에 대한 최초 컬럼 생성
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyceler_item,parent,false);
        CustomViewHolder holder= new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //매칭을함
        //CustomViewHolder myViewHolder = (CustomViewHolder) holder;
        holder.tvName.setText(arrayList.get(position).getFoodname());
        holder.tvCompany.setText(arrayList.get(position).getCompany());
        holder.checkBox.setChecked(arrayList.get(position).isSelected());
        holder.checkBox.setTag(arrayList.get(position));

        //레이아웃 클릭시 해당 음식 상세정보 보여주기
        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, DetailFoodInfo.class);
                intent.putExtra("foodInfo",arrayList);
                context.startActivity(intent);
            }
        });


        //체크박스기능
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                food contact = (food) cb.getTag();

                contact.setSelected(cb.isChecked());
                arrayList.get(position).setSelected(cb.isChecked());

                Toast.makeText(
                        v.getContext(),
                        "Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (arrayList!=null? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView tvName;
        TextView tvCompany;
        //레이아웃 클릭시 상세정보 액티비티 이동
        ViewGroup layout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName=itemView.findViewById(R.id.Food_name);
            this.tvCompany=itemView.findViewById(R.id.company);
            this.checkBox=itemView.findViewById(R.id.check_box);
            this.layout= (ViewGroup)itemView.findViewById(R.id.li1);
        }
    }

}
