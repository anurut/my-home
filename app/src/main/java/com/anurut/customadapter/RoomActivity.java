package com.anurut.customadapter;

import android.content.Intent;
import android.os.Bundle;

import com.anurut.customadapter.button.ButtonAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

public class RoomActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);


        //toolbar = findViewById(R.id.room_toolbar);
        //toolbar.setNavigationIcon(R.drawable.ab);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MSG);

        //ArrayList<ButtonData> buttonData = ;

        TextView header = findViewById(R.id.headerText);
        header.setText(message);

        recyclerView = findViewById(R.id.buttonView);
        recyclerView.setHasFixedSize(true);
        ButtonAdapter adapter = new ButtonAdapter(Data.getButtonDataArrayList(message), RoomActivity.this);
        if(Data.getButtonDataArrayList(message).size() <=2)
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        recyclerView.setAdapter(adapter);

    }

    public void onBackButtonClick(View view){
        finish();
    }

}
