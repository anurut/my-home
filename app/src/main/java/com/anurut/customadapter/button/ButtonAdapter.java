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

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyListViewHolder>{

    private ButtonData[] listArray;

    public ButtonAdapter(ButtonData[] listArray){
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

        final ButtonData buttonData = listArray[position];
        holder.textView.setText(buttonData.getButtonName());
        holder.imageButton.setImageResource(buttonData.getButtonImgId());
        holder.imageButton.setTag(buttonData.getButtonName());
        holder.textView.setTag(buttonData.getButtonName());
        holder.constraintLayout.setTag(buttonData.getButtonName());
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