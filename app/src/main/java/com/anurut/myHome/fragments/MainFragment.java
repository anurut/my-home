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
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;
import com.anurut.myHome.model.Room;
import com.anurut.myHome.adapters.RoomAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.anurut.myHome.R.string.mqtt_status_text;

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

        bindViews();
        setMqttStatusConnecting();

        mViewModel.getMqttStatus().observe(this, mqttStatusObserver);

        setAdapter();

        mViewModel.getRoomDataArraylist().observe(this, roomDataArraylistObserver);
    }

    private void bindViews() {
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.main_fragment_recyclerView);
        recyclerView.setHasFixedSize(true);

        mqttStatus = getView().findViewById(R.id.main_mqtt_status);
    }

    private void setAdapter() {
        adapter = new RoomAdapter(Data.getRoomDataAttayList());
        if (Data.getRoomDataAttayList().size() <= 2)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private Observer<String> mqttStatusObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {

            if (s != null) {
                if (s.equalsIgnoreCase("connected")) {
                    mqttStatus.setText(s);
                    mqttStatus.getAnimation().cancel();
                } else
                    setMqttStatusConnecting();
            }
        }
    };

    private Observer<ArrayList<Room>> roomDataArraylistObserver = new Observer<ArrayList<Room>>() {
        @Override
        public void onChanged(ArrayList<Room> rooms) {
            adapter.notifyDataSetChanged();
        }
    };

    private void setMqttStatusConnecting() {
        mqttStatus.setText(mqtt_status_text);
        mqttStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_out));
    }
}
