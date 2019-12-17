package com.anurut.myHome.adapters;


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
import com.anurut.myHome.model.Button;

import java.util.ArrayList;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyListViewHolder> {

    private ArrayList<Button> buttonData;
    //private Activity activity;
    //private String mqttStatus;

    public ButtonAdapter(ArrayList<Button> buttonData) {

        this.buttonData = buttonData;
        //this.activity = activity;
        //this.mqttStatus = mqttStatus;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mqtt_button_layout, parent, false);
        return new MyListViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyListViewHolder holder, int position) {
        final Button myButton = buttonData.get(position);
        System.out.println("Get Button State :" + myButton.getButtonState());

        switch (myButton.getButtonState().toLowerCase()) {
            case "off":
                holder.buttonName.setText(myButton.getButtonName());
                holder.constraintLayout.setBackgroundResource(0);
                holder.imageButton.setImageResource(myButton.getDefaultImageId());
                break;
            case "offline":
                holder.imageButton.setEnabled(false);
                holder.buttonName.setText(R.string.button_unavailable);
                break;
            case "online":
                holder.imageButton.setEnabled(true);
                holder.buttonName.setText(myButton.getButtonName());
                break;
            case "on":
                holder.buttonName.setText(myButton.getButtonName());
                holder.constraintLayout.setBackgroundResource(R.drawable.button_bg_60);
                holder.imageButton.setImageResource(myButton.getImageIdStateOn());
                //applying animation on fan
                if (myButton.getButtonName().contains("fan")) {
                    holder.imageButton.startAnimation(
                            AnimationUtils.loadAnimation(MainActivity.mainActivity,R.anim.rotation)//R.anim.rotation
                    );
                }
                break;
            default:
                Log.d("mqtt", "Waiting for button state update");
                holder.buttonName.setText(myButton.getButtonName());
                holder.constraintLayout.setBackgroundResource(0);
                holder.imageButton.setImageResource(myButton.getDefaultImageId());
                break;
        }

        holder.imageButton.setTag(myButton.getButtonName());
        holder.buttonName.setTag(myButton.getButtonName());
        holder.constraintLayout.setTag(myButton.getButtonName());


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("On Click", v.getTag().toString());
                MainActivity.mainActivity.publish(myButton.getCommandTopic(), myButton.getButtonState());
                holder.imageButton.setImageResource(myButton.getImageIdStateIdle());
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