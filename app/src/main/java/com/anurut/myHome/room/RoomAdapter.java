package com.anurut.myHome.room;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anurut.myHome.MainActivity;
import com.anurut.myHome.R;
import com.anurut.myHome.RoomActivity;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

    private ArrayList<RoomData> roomData;
    public Context mContext;

    public RoomAdapter(Context mContext,ArrayList<RoomData> roomData){
        this.roomData = roomData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View fragmentItem = layoutInflater.inflate(R.layout.mqtt_roomicon_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(fragmentItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RoomData myRoomData =  roomData.get(position);

        holder.roomIcon.setImageResource(myRoomData.getRoomImageId());
        holder.roomIcon.setTag(myRoomData.getRoomName());
        holder.roomName.setText(myRoomData.getRoomName());
        holder.roomIconLayout.setTag(myRoomData.getRoomName());
        holder.roomIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("On Click",v.getTag().toString());

                //MainActivity.mainActivity.onRoomIconClick(v);
//                ClickAction clk = new ClickAction();
//                clk.onRoomIconClick(v,MainActivity.mainActivity);

                String MSG = "com.anurut.myHome.ROOMS";
                Intent intent = new Intent(mContext, RoomActivity.class);
                String roomName = holder.roomIcon.getTag().toString();
                MainActivity.mainActivity.buttonTagHold  =  roomName;

                //intent.putExtra(MSG, view.getTag().toString());

                intent.putExtra(MSG,roomName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView roomIcon;
        public TextView roomName;
        public ConstraintLayout roomIconLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.roomIcon = itemView.findViewById(R.id.room);
            this.roomName = itemView.findViewById(R.id.roomTextView);
            this.roomIconLayout=itemView.findViewById(R.id.roomIconConstraint);
        }
    }
}
