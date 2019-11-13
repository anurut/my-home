package com.anurut.customadapter.button;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.anurut.customadapter.R;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewHolder>{

    private MyListData[] listArray;

    public MyListAdapter(MyListData[] listArray){
        this.listArray = listArray;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mqtt_button_layout,parent,false);
        MyListViewHolder viewHolder = new MyListViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {

        final MyListData myListData = listArray[position];
        holder.textView.setText(myListData.getButtonName());
        holder.imageButton.setImageResource(myListData.getButtonImgId());
        holder.imageButton.setTag(myListData.getButtonName());
        holder.textView.setTag(myListData.getButtonName());
        holder.constraintLayout.setTag(myListData.getButtonName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("On Click",v.getTag().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listArray.length;
    }

    class MyListViewHolder extends ViewHolder {

        public ImageButton imageButton;
        public TextView textView;
        public androidx.constraintlayout.widget.ConstraintLayout constraintLayout;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.button);
            this.textView = itemView.findViewById(R.id.textView);
            this.constraintLayout = itemView.findViewById(R.id.imageButtonConstraint);
        }
    }
}