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

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyListViewHolder> {

    private ArrayList<ButtonData> buttonData;
    private Activity activity;
    //private String mqttStatus;

    public ButtonAdapter(ArrayList<ButtonData> buttonData, Activity activity) {

        this.buttonData = buttonData;
        this.activity = activity;
        //this.mqttStatus = mqttStatus;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View listItem = layoutInflater.inflate(R.layout.mqtt_button_layout, parent, false);
        return new MyListViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyListViewHolder holder, int position) {
        final ButtonData myButtonData = buttonData.get(position);
        System.out.println("Get Button State :" + myButtonData.getButtonState());

        switch (myButtonData.getButtonState().toLowerCase()) {
            case "off":
                holder.buttonName.setText(myButtonData.getButtonName());
                holder.constraintLayout.setBackgroundResource(0);
                holder.imageButton.setImageResource(myButtonData.getDefaultImageId());
                break;
            case "offline":
                holder.imageButton.setEnabled(false);
                holder.buttonName.setText(R.string.button_unavailable);
                break;
            case "online":
                holder.imageButton.setEnabled(true);
                holder.buttonName.setText(myButtonData.getButtonName());
                break;
            case "on":
                holder.buttonName.setText(myButtonData.getButtonName());
                holder.constraintLayout.setBackgroundResource(R.drawable.button_bg_60);
                holder.imageButton.setImageResource(myButtonData.getImageIdStateOn());
                //applying animation on fan
                if (myButtonData.getButtonName().contains("fan")) {
                    holder.imageButton.startAnimation(
                            AnimationUtils.loadAnimation(RoomActivity.roomActivity, R.anim.rotation)
                    );
                }
                break;
            default:
                Log.d("mqtt", "Waiting for button state update");
                holder.buttonName.setText(myButtonData.getButtonName());
                holder.constraintLayout.setBackgroundResource(0);
                holder.imageButton.setImageResource(myButtonData.getDefaultImageId());
                break;
        }

        holder.imageButton.setTag(myButtonData.getButtonName());
        holder.buttonName.setTag(myButtonData.getButtonName());
        holder.constraintLayout.setTag(myButtonData.getButtonName());


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("On Click", v.getTag().toString());
                MainActivity.mainActivity.publish(myButtonData.getCommandTopic(), myButtonData.getButtonState());
                holder.imageButton.setImageResource(myButtonData.getImageIdStateIdle());
            }
        });
    }


    @Override
    public int getItemCount() {
        return buttonData.size();
    }

    class MyListViewHolder extends ViewHolder {

        public ImageButton imageButton;
        public TextView buttonName;
        public TextView mqttStatus;
        public androidx.constraintlayout.widget.ConstraintLayout constraintLayout;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.button);
            this.buttonName = itemView.findViewById(R.id.textView);
            this.constraintLayout = itemView.findViewById(R.id.imageButtonConstraint);
            this.mqttStatus = itemView.findViewById(R.id.mqttStatus);
        }
    }
}