package com.example.daily_function;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    TextView hits;
    TextView gets;

    ViewHolder(View itemView)
    {
        super(itemView);

        title = itemView.findViewById(R.id.board_title);
        hits = itemView.findViewById(R.id.board_hit);
        gets = itemView.findViewById(R.id.board_get);
    }
}