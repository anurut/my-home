package com.anurut.customadapter.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.anurut.customadapter.RoomActivity;


public class ClickAction {

    public void onRoomIconClick(View view, Activity activity){

        String MSG = "com.anurut.customadapter.ROOMS";
        Intent intent = new Intent(activity, RoomActivity.class);
        String roomName = view.getTag().toString();

        //intent.putExtra(MSG, view.getTag().toString());

        intent.putExtra(MSG,roomName);
        activity.startActivity(intent);
    }
}
