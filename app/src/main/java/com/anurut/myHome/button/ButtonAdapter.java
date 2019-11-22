package com.anurut.myHome.button;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.anurut.myHome.MainActivity;
import com.anurut.myHome.R;
import com.anurut.myHome.RoomActivity;

import java.util.ArrayList;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyListViewHolder>{

    private ArrayList<ButtonData> buttonData;
    private Activity activity;
    //private String mqttStatus;

    public ButtonAdapter(ArrayList<ButtonData> buttonData, Activity activity){

        this.buttonData = buttonData;
        this.activity = activity;
        //this.mqttStatus = mqttStatus;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View listItem = layoutInflater.inflate(R.layout.mqtt_button_layout,parent,false);
        MyListViewHolder viewHolder = new MyListViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyListViewHolder holder, int position) {
        final ButtonData mybuttonData = buttonData.get(position);
        System.out.println("Get Button State :" + mybuttonData.getButtonState());

        if (mybuttonData.getButtonState().equalsIgnoreCase("OFF")){
            holder.constraintLayout.setBackgroundResource(0);
            holder.imageButton.setImageResource(mybuttonData.getDefaultImageId());
        }
        else {
            holder.constraintLayout.setBackgroundResource(R.drawable.button_bg_60);
            holder.imageButton.setImageResource(mybuttonData.getImageIdStateOn());
            //applying animation on fan
            if(mybuttonData.getButtonName().contains("fan")){
                holder.imageButton.startAnimation(
                        AnimationUtils.loadAnimation(RoomActivity.roomActivity,R.anim.rotation)
                );
            }
        }

        holder.textView.setText(mybuttonData.getButtonName());
        holder.imageButton.setTag(mybuttonData.getButtonName());
        holder.textView.setTag(mybuttonData.getButtonName());
        holder.constraintLayout.setTag(mybuttonData.getButtonName());


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("On Click",v.getTag().toString());
                MainActivity.mainActivity.publish(mybuttonData.getCommandTopic(),mybuttonData.getButtonState());
                holder.imageButton.setImageResource(mybuttonData.getImageIdStateIdle());
            }
        });


    }



    @Override
    public int getItemCount() {
        return buttonData.size();
    }

    class MyListViewHolder extends ViewHolder {

        public ImageButton imageButton;
        public TextView textView;
        public TextView mqttStatus;
        public androidx.constraintlayout.widget.ConstraintLayout constraintLayout;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.button);
            this.textView = itemView.findViewById(R.id.textView);
            this.constraintLayout = itemView.findViewById(R.id.imageButtonConstraint);
            this.mqttStatus = itemView.findViewById(R.id.mqttStatus);
        }
    }
}