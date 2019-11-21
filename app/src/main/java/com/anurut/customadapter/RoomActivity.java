package com.anurut.customadapter;

import android.content.Intent;
import android.os.Bundle;

import com.anurut.customadapter.button.ButtonAdapter;
import com.anurut.customadapter.button.ButtonData;
import com.anurut.customadapter.button.ButtonState;
import com.anurut.customadapter.helper.MqttHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public static RoomActivity roomActivity;
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

    public void refreshData(){

        adapter.notifyDataSetChanged();

    }



}
