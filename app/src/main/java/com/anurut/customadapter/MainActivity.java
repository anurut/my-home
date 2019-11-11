package com.anurut.customadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.anurut.customadapter.button.MyListAdapter;
import com.anurut.customadapter.button.MyListData;

public class MainActivity extends AppCompatActivity {

    public MyListData[] myListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListData = new MyListData[]{

                new MyListData("fan",R.drawable.power_black_24dp),
                new MyListData("tube light",R.drawable.power_black_24dp),
                new MyListData("night light",R.drawable.power_black_24dp),
                new MyListData("dummy light",R.drawable.power_black_24dp)

        };

        RecyclerView recyclerView = findViewById(R.id.roomsRecyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }
}
