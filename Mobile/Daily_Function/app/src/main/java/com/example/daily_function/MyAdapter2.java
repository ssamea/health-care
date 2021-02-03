package com.example.daily_function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class MyAdapter2  extends RecyclerView.Adapter<MyAdapter2.CustomViewHolder> implements Serializable {
    private ArrayList<food> food_list;
    private Context context;

    public MyAdapter2(ArrayList<food> food_list, Context context) {
        this.food_list = food_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //아이템에 대한 최초 컬럼 생성
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyceler_item2,parent,false);
        CustomViewHolder holder= new MyAdapter2.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvName.setText(food_list.get(position).getFoodname());
        holder.tvKcal.setText(food_list.get(position).getKcal()+"kcal");
    }

    @Override
    public int getItemCount() {
        return (null != food_list ? food_list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvKcal;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName=itemView.findViewById(R.id.tv_Food_name2);
            this.tvKcal=itemView.findViewById(R.id.tv_kcal2);

        }
    }

}
