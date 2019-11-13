package com.anurut.customadapter.helper;

import android.util.Log;

import com.anurut.customadapter.Interface.CallResponse;
import com.anurut.customadapter.R;
import com.anurut.customadapter.room.RoomData;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MqttMessageReceived {

    private MqttMessage message;
    private String topic;
    public CallResponse callResponse;
    public ArrayList<RoomData> roomDatas;


    public MqttMessageReceived(String topic, MqttMessage message, CallResponse callResponse){

        this.message = message;
        this.topic = topic;
        this.callResponse =  callResponse;
        roomDatas =  new ArrayList<>();



        setRooms(topic);
    }

    public void setRooms(String topic){


        String room_name = "";
        String roomName = "";
        int roomImageId = 0;

        String currentString = topic;
        String[] separated = currentString.split("/");

        roomName =  separated[1];

        Log.d("mqtt", "Room added: " + roomName);


        if(topic.contains("LWT")){

            switch (roomName.toLowerCase()){
                case "masterbedroom":
                    room_name= "master bedroom";
                    roomImageId = R.drawable.bedroom_black_24dp;
                    break;
                case "masterbathroom":
                    room_name = "master bathroom";
                    roomImageId = R.drawable.bathroom_black_24dp;
                    break;
                case "guestbedroom":
                    room_name = "guest bedroom";
                    roomImageId = R.drawable.bedroom_black_24dp;
                    break;
                case "guestbathroom":
                    room_name = "guest bathroom";
                    roomImageId = R.drawable.bathroom_black_24dp;
                    break;
                case "kitchen":
                    room_name = "kitchen";
                    roomImageId = R.drawable.kitchen_black_24dp;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + roomName.toLowerCase());
            }

            RoomData roomData =  new RoomData(room_name,roomImageId);
            roomDatas.add(roomData);
            callResponse.getResponse(roomDatas);

        }
    }
}
