package com.anurut.customadapter.helper;

import android.util.Log;

import com.anurut.customadapter.Data;
import com.anurut.customadapter.Interface.CallResponse;
import com.anurut.customadapter.R;
import com.anurut.customadapter.button.ButtonData;
import com.anurut.customadapter.room.RoomData;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.anurut.customadapter.Data.getRoomDataAttayList;
import static com.anurut.customadapter.Data.getRoomName;

public class MqttMessageReceived {

    private MqttMessage message;
    private String topic;
    public CallResponse callResponse;
    public ArrayList<ButtonData> buttonData;
    public HashMap<String,ArrayList<ButtonData>> roomWiseButtonData;


    public MqttMessageReceived(String topic, MqttMessage message, CallResponse callResponse) throws JSONException {

        this.message = message;
        this.topic = topic;
        this.callResponse =  callResponse;

        String payload = message.toString();

        if(topic.contains("tele") && payload.toLowerCase().equals("online")) {
            setRooms(topic);
        }
        if(topic.contains("tele") && payload.toLowerCase().equals("offline")) {
            deleteRoom(topic);
        }

        setRoomButtons(topic, this.message);
    }

    private void setRooms(String topic) {

        String room_name;
        String roomName = getRoomName(topic);
        int roomImageId;

            // set room name and icon
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

            if(!Data.roomAlreadyExist(room_name)){

                Data.addToRoomDataArrayList(new RoomData(room_name,roomImageId));
                Log.d("mqtt", "Room added: " + roomName);
            }

            callResponse.getResponse(getRoomDataAttayList());
    }

    private void deleteRoom(String topic) throws JSONException {
        String room_name = "";
        String roomName = getRoomName(topic);
        int roomImageId = 0;

        // set room name and icon
        switch (roomName.toLowerCase()){
            case "masterbedroom":
                room_name= "master bedroom";
                roomImageId = R.drawable.bedroom_black_24dp;
                //setRoomButtons(topic, message);
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

        if(Data.roomAlreadyExist(room_name)){

            Data.deleteRoomDataFromArrayList(room_name);

            Log.d("mqtt", "Room deleted: " + roomName);
            Log.d("mqtt", "Rooms remained: " + getRoomDataAttayList().toString());
        }

        callResponse.getResponse(getRoomDataAttayList());
    }

    private void setRoomButtons(String topic, MqttMessage message) throws JSONException {

        String msgPayload = new String(message.getPayload());
        JSONObject payload = new JSONObject(msgPayload);

        if(payload.has("Time")){
            switch (topic.toLowerCase()){
                case "stat/masterbedroom/result":

                    if(payload.has("POWER1")){
                        Log.d("mqtt", "Setting up POWER1");
                    }

                    if(payload.has("POWER2")){
                        Log.d("mqtt", "Setting up POWER2");
                    }

                    if(payload.has("POWER3")){
                        Log.d("mqtt", "Setting up POWER3");
                    }

                    if(payload.has("POWER4")){
                        Log.d("mqtt", "Setting up POWER4");
                    }
                    break;
                case "stat/kitchen/result":
                    if(payload.has("POWER1")){
                        //
                    }

                    if(payload.has("POWER2")){
                        //
                    }

                    if(payload.has("POWER3")){
                        //
                    }

                    if(payload.has("POWER4")){
                        //
                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + topic.toLowerCase());
            }
        }

    }
}
