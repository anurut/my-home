package com.anurut.customadapter.helper;

import android.util.Log;

import com.anurut.customadapter.Data;
import com.anurut.customadapter.Interface.CallResponse;
import com.anurut.customadapter.R;
import com.anurut.customadapter.button.ButtonData;
import com.anurut.customadapter.button.ButtonState;
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
                case "master bedroom":
                    roomImageId = R.drawable.bedroom_black_24dp;

                    break;
                case "master bathroom":
                    roomImageId = R.drawable.bathroom_black_24dp;
                    break;
                case "guest bedroom":
                    roomImageId = R.drawable.bedroom_black_24dp;
                    break;
                case "guest bathroom":
                    roomImageId = R.drawable.bathroom_black_24dp;
                    break;
                case "kitchen":
                    roomImageId = R.drawable.kitchen_black_24dp;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + roomName.toLowerCase());
            }

            if(!Data.roomAlreadyExist(roomName)){

                Data.addToRoomDataArrayList(new RoomData(roomName,roomImageId));
                Log.d("mqtt", "Room added: " + roomName);
            }

            callResponse.getResponse(getRoomDataAttayList());
    }

    private void deleteRoom(String topic) {
        String room_name = "";
        String roomName = getRoomName(topic);
        int roomImageId = 0;

        if(Data.roomAlreadyExist(roomName)){

            Data.deleteRoomDataFromArrayList(roomName);

            Log.d("mqtt", "Room deleted: " + roomName);
            Log.d("mqtt", "Rooms remained: " + getRoomDataAttayList().toString());
        }

        callResponse.getResponse(getRoomDataAttayList());
    }

    private void setRoomButtons(String topic, MqttMessage message) throws JSONException {

        String msgPayload = new String(message.getPayload());
        JSONObject payload = new JSONObject(msgPayload);
        ArrayList<ButtonData> buttonData =  new ArrayList<>();
        String roomName = Data.getRoomName(topic);

        if(payload.has("Time")){
            switch (topic.toLowerCase()){
                case "stat/masterbedroom/result":

                    if(payload.has("POWER1")){
                        Log.d("mqtt", "Setting up POWER1");
                        buttonData.add(new ButtonData("tube light", R.drawable.power_black_24dp));
                        ButtonState.setButtonState(roomName,payload.getString("POWER1"));
//                        //Log.d("mqtt",)
                    }
                    if(payload.has("POWER2")){
                        Log.d("mqtt", "Setting up POWER2");
                        buttonData.add(new ButtonData("ceiling fan", R.drawable.power_black_24dp));
                        ButtonState.setButtonState(roomName,payload.getString("POWER2"));
                    }

                    if(payload.has("POWER3")){
                        Log.d("mqtt", "Setting up POWER3");
                        buttonData.add(new ButtonData("night light", R.drawable.power_black_24dp));
                        ButtonState.setButtonState(roomName,payload.getString("POWER3"));
                    }

                    if(payload.has("POWER4")){
                        Log.d("mqtt", "Setting up POWER4");
                        buttonData.add(new ButtonData("dummy light", R.drawable.power_black_24dp));
                        ButtonState.setButtonState(roomName,payload.getString("POWER4"));
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

            Data.addToButtonDataMap(roomName, buttonData);
        }

    }
}
