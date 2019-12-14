package com.anurut.myHome.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;
import com.anurut.myHome.room.RoomAdapter;
import com.anurut.myHome.room.RoomData;

import java.security.PublicKey;
import java.util.ArrayList;

public class MainPage extends Fragment {
    RecyclerView recyclerView;
   public static RecyclerView.Adapter adapter;
   public static ArrayList<RoomData> roomDataArraylist;
   private static TextView mqttStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setHasFixedSize(true);

        mqttStatus = view.findViewById(R.id.mqtt_status);
        mqttStatus.setText("Connecting");

        roomDataArraylist = new ArrayList<>();
        roomDataArraylist.addAll(Data.getRoomDataAttayList());

        adapter = new RoomAdapter(getContext(), roomDataArraylist);
        if (roomDataArraylist.size() <= 2)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMqttStatus();
        refreshData();
    }

    public static void refreshMqttStatus(){
        mqttStatus.setText(Data.getMqttStatus());
    }

    public static void refreshData(){
        roomDataArraylist.clear();
        roomDataArraylist.addAll(Data.getRoomDataAttayList());
        adapter.notifyDataSetChanged();
    }


}
