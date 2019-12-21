package com.anurut.myHome.adapters;

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
import com.anurut.myHome.fragments.RoomFragment;
import com.anurut.myHome.model.Room;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private ArrayList<Room> roomData;

    public RoomAdapter(ArrayList<Room> roomData) {
        this.roomData = roomData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View fragmentItem = layoutInflater.inflate(R.layout.mqtt_roomicon_layout, parent, false);
        return new ViewHolder(fragmentItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Room myRoom = roomData.get(position);

        holder.roomIcon.setBackgroundResource(myRoom.getRoomImageId());
        holder.roomIcon.setTag(myRoom.getRoomName());
        holder.roomName.setText(myRoom.getRoomName());
        holder.roomIconLayout.setTag(myRoom.getRoomName());
        holder.roomIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("On Click", v.getTag().toString());
                String roomName = holder.roomIcon.getTag().toString();
                MainActivity.mainActivity.changeFragment(new RoomFragment(), roomName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView roomIcon;
        TextView roomName;
        ConstraintLayout roomIconLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.roomIcon = itemView.findViewById(R.id.room_icon);
            this.roomName = itemView.findViewById(R.id.roomTextView);
            this.roomIconLayout = itemView.findViewById(R.id.roomIconConstraint);
        }
    }
}
