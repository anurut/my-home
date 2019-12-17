package com.anurut.myHome.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;
import com.anurut.myHome.model.Room;
import com.anurut.myHome.room.RoomAdapter;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView mqttStatus;
    private MainViewModel mViewModel;
    private RecyclerView.Adapter adapter;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //mViewModel.setRoomDataArraylist(Data.getRoomDataAttayList());

        recyclerView = getView().findViewById(R.id.main_fragment_recyclerView);
        recyclerView.setHasFixedSize(true);

        mqttStatus = getView().findViewById(R.id.main_mqtt_status);

        mViewModel.getMqttStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mqttStatus.setText(s);
            }
        });



        adapter = new RoomAdapter(Data.getRoomDataAttayList());
        if (Data.getRoomDataAttayList().size() <= 2)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        mViewModel.getRoomDataArraylist().observe(this, new Observer<ArrayList<Room>>() {
            @Override
            public void onChanged(ArrayList<Room> roomData) {
               adapter.notifyDataSetChanged();
            }
        });
    }

}
