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
import android.widget.ImageButton;
import android.widget.TextView;

import com.anurut.myHome.Data;
import com.anurut.myHome.MainActivity;
import com.anurut.myHome.R;
import com.anurut.myHome.adapters.ButtonAdapter;
import com.anurut.myHome.model.Button;

import java.util.ArrayList;

public class RoomFragment extends Fragment {

    private RoomViewModel mViewModel;
    private RecyclerView recyclerView;
    private TextView mqttStatus;
    private RecyclerView.Adapter adapter;
    private ImageButton backButton;

    public static RoomFragment newInstance() {
        return new RoomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        MainActivity.mainActivity.toolbar.setVisibility(View.GONE);

        return inflater.inflate(R.layout.room_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);

        final String roomName = getArguments().getString("data");
        mViewModel.setRoomName(roomName);

        //RoomViewModel.setButtonArrayList(Data.getButtonDataArrayList(roomName));

        recyclerView = getView().findViewById(R.id.buttonView);
        recyclerView.setHasFixedSize(true);

        mqttStatus = getView().findViewById(R.id.mqttStatus);
        backButton = getView().findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.changeFragment(new MainFragment());
                MainActivity.mainActivity.toolbar.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getMqttStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mqttStatus.setText(s);
            }
        });


        adapter = new ButtonAdapter(Data.getButtonDataArrayList(roomName));
        if(Data.getButtonDataArrayList(roomName).size() <=2)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        recyclerView.setAdapter(adapter);

        mViewModel.getButtonArrayList().observe(this, new Observer<ArrayList<Button>>() {
            @Override
            public void onChanged(ArrayList<Button> buttons) {
               adapter.notifyDataSetChanged();
            }
        });
    }

}
