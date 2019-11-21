package com.anurut.customadapter.button;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.anurut.customadapter.MainActivity;
import com.anurut.customadapter.R;
import com.anurut.customadapter.RoomActivity;
import com.anurut.customadapter.helper.MqttHelper;

import java.util.ArrayList;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.MyListViewHolder>{

    private ArrayList<ButtonData> buttonData;
    private Activity activity;

    public ButtonAdapter(ArrayList<ButtonData> buttonData, Activity activity){

        this.buttonData = buttonData;
        this.activity = activity;
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
            holder.constraintLayout.setBackgroundColor(activity.getResources().getColor(R.color.buttonColor));
        }
        else {
            holder.constraintLayout.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
        }


        holder.textView.setText(mybuttonData.getButtonName());
        holder.imageButton.setImageResource(mybuttonData.getButtonImgId());
        holder.imageButton.setTag(mybuttonData.getButtonName());
        holder.textView.setTag(mybuttonData.getButtonName());
        holder.constraintLayout.setTag(mybuttonData.getButtonName());

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("On Click",v.getTag().toString());
                MainActivity.mainActivity.publish(mybuttonData.getCommandTopic(),mybuttonData.getButtonState());
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
        public androidx.constraintlayout.widget.ConstraintLayout constraintLayout;

        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.button);
            this.textView = itemView.findViewById(R.id.textView);
            this.constraintLayout = itemView.findViewById(R.id.imageButtonConstraint);
        }
    }
}