package com.anurut.myHome;

import android.content.Intent;
import android.os.Bundle;

import com.anurut.myHome.button.ButtonAdapter;
import com.anurut.myHome.button.ButtonData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static com.anurut.myHome.Data.getMqttStatus;

public class RoomActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public static RoomActivity roomActivity;
    private TextView mqttStatus;
    public RecyclerView.Adapter adapter;
    public ArrayList<ButtonData> stateArrayList;
    //private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomActivity =  this;

        //toolbar = findViewById(R.id.room_toolbar);
        //toolbar.setNavigationIcon(R.drawable.ab);


        MainActivity.mainActivity.activityStateCheck = 1;

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MSG);

        //ArrayList<ButtonData> buttonData = ;

        mqttStatus = findViewById(R.id.mqttStatus);
        mqttStatus.setText(Data.getMqttStatus());

        TextView header = findViewById(R.id.headerText);
        header.setText(message);

        recyclerView = findViewById(R.id.buttonView);
        recyclerView.setHasFixedSize(true);

        stateArrayList =  new ArrayList<>();

        stateArrayList.addAll(Data.getButtonDataArrayList(message));

        adapter = new ButtonAdapter(stateArrayList, RoomActivity.this);
        if(Data.getButtonDataArrayList(message).size() <=2)
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        recyclerView.setAdapter(adapter);

    }

    public void onBackButtonClick(View view){
        MainActivity.mainActivity.activityStateCheck = 0;
        finish();
    }

    public void refreshData(){ adapter.notifyDataSetChanged(); }

}
