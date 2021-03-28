package com.example.daily_function;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class RecyclerBoardAdapter extends FirebaseRecyclerAdapter<BoardData,RecyclerBoardAdapter.MyViewHolder> { //리사이클러 뷰 꾸미기

    private Intent intent;


    public RecyclerBoardAdapter(@NonNull FirebaseRecyclerOptions<BoardData> options ) {
        super(options);
    }

    @NonNull
    @Override
    public RecyclerBoardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_board,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull BoardData board) { //각항목 클릭시 이벤트 및 바인드 내용꾸미기
        holder.title.setText(board.getTitle());
        holder.hit.setText(String.valueOf(board.getHits()));
        holder.get.setText(String.valueOf(board.getGet()));

        holder.title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent= new Intent(v.getContext(),BoardDetailActivity.class);

                intent.putExtra("board",board);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });

        holder.hit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent= new Intent(v.getContext(),BoardDetailActivity.class);

                intent.putExtra("board",board);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);
            }
        });

        holder.get.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent= new Intent(v.getContext(),BoardDetailActivity.class);

                intent.putExtra("board",board);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);

            }
        });



    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{ //리사이클러뷰 꾸미기
        TextView title,hit,get;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            title=itemView.findViewById(R.id.board_title);
            hit=itemView.findViewById(R.id.board_hit);
            get=itemView.findViewById(R.id.board_get);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getContext(),BoardDetailActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}


