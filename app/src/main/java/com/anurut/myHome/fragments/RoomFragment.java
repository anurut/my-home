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
    private String roomName;

    public static RoomFragment newInstance() {
        return new RoomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        MainActivity.mainActivity.toolbar.setVisibility(View.GONE);
        roomName = getArguments().getString("data");
        return inflater.inflate(R.layout.room_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RoomViewModel.class);

        mViewModel.setRoomName(roomName);

        bindViews();

        backButton.setOnClickListener(onBackButtonClickListener);

        mViewModel.getMqttStatus().observe(this, mqttStatusObserver);

        setAdapter();

        mViewModel.getButtonArrayList().observe(this, adapterObserver);
    }







    void setAdapter() {
        adapter = new ButtonAdapter(Data.getButtonDataArrayList(roomName));
        if (Data.getButtonDataArrayList(roomName).size() <= 2)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(adapter);
    }

    private void bindViews(){
        recyclerView = getView().findViewById(R.id.buttonView);
        recyclerView.setHasFixedSize(true);

        mqttStatus = getView().findViewById(R.id.mqttStatus);
        backButton = getView().findViewById(R.id.backButton);
    }

    private View.OnClickListener onBackButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.mainActivity.changeFragment(new MainFragment());
            MainActivity.mainActivity.toolbar.setVisibility(View.VISIBLE);
        }
    };

    private Observer<String> mqttStatusObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            mqttStatus.setText(s);
        }
    };

    private Observer<ArrayList<Button>> adapterObserver = new Observer<ArrayList<Button>>() {
        @Override
        public void onChanged(ArrayList<Button> buttons) {
            adapter.notifyDataSetChanged();
        }
    };

}
