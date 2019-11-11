package com.anurut.customadapter.room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anurut.customadapter.R;
import com.anurut.customadapter.button.MyListAdapter;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{

    private RoomData[] roomData;

    public RoomAdapter(RoomData[] roomData){
        this.roomData = roomData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View fragmentItem = layoutInflater.inflate(R.layout.mqtt_room_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(fragmentItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RoomData myRoomData = roomData[position];

        holder.headerTextView.setText(myRoomData.getRoomName());
        //holder.statusTextView.setText(myRoomData.getMqttStatus());
        MyListAdapter adapter = new MyListAdapter(myRoomData.getButtons());
        holder.buttonView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return roomData.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView headerTextView;
        public TextView statusTextView;
        public ConstraintLayout roomLayout;
        public RecyclerView buttonView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.headerTextView = itemView.findViewById(R.id.headerText);
            this.statusTextView = itemView.findViewById(R.id.mqttStatus);
            this.buttonView = itemView.findViewById(R.id.buttonView);
            this.roomLayout = itemView.findViewById(R.id.roomLayout);
        }
    }
}
