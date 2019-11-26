package com.anurut.myHome.fragments;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;
import com.anurut.myHome.room.RoomAdapter;
import com.anurut.myHome.room.RoomData;

import java.util.ArrayList;

public class MainPage extends Fragment {
    RecyclerView recyclerView;
   public static RecyclerView.Adapter adapter;
   public static ArrayList<RoomData> roomDataArraylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setHasFixedSize(true);


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


    public static void refreshData(){

        roomDataArraylist.clear();
        roomDataArraylist.addAll(Data.getRoomDataAttayList());
        adapter.notifyDataSetChanged();
    }


}
