package com.anurut.customadapter;

import android.util.Log;

import com.anurut.customadapter.button.ButtonData;
import com.anurut.customadapter.room.RoomData;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    private ArrayList<ButtonData> buttonDataArrayList = new ArrayList<>();
    private static ArrayList<RoomData> roomDataArrayList = new ArrayList<>();
    private static HashMap<String, ArrayList<ButtonData>> buttonDataMap = new HashMap<>();

    public static ArrayList<RoomData> getRoomDataAttayList(){  return roomDataArrayList;    }

    // Returns true if room name already exists in roomDataArrayList
    public static boolean roomAlreadyExist(String roomName){

        RoomData roomData;

        if(!(roomDataArrayList.size() == 0)){
            for(int i=0; i<roomDataArrayList.size(); i++){
                roomData = roomDataArrayList.get(i);
                if(roomData.getRoomName().equals(roomName.toLowerCase())) return true;
            }
        }
        return false;
    }


    public static void addToRoomDataArrayList(RoomData roomData) {
        Data.roomDataArrayList.add(roomData);
    }

    public static void deleteRoomDataFromArrayList(String roomName) {
        Data.roomDataArrayList.remove(new Data().getValueIndex(roomName));
    }

    public static String getRoomName(String topic){
        String roomName;
        String currentString = topic;
        String[] separated = currentString.split("/");
        String room_name =  separated[1];

        switch (room_name){
            case "masterbedroom":
                roomName = "master bedroom";
                break;
            case "masterbathroom":
                roomName = "master bathroom";
            case "guestbedroom":
                roomName = "guest bedroom";
                break;
            case "guestbathroom":
                roomName = "guest bathroom";
                break;
            case "kitchen":
                roomName = "kitchen";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + room_name);
        }
        return roomName;
    }

    public static String getPayloadKey(String topic){

        String currentString = topic;
        String[] separated = currentString.split("/");
        String payloadKey =  separated[2];
        return payloadKey;
    }


    int getValueIndex(String roomName) {

        RoomData roomData;

        if (!(roomDataArrayList.size() == 0)) {
            for (int i = 0; i < roomDataArrayList.size(); i++) {
                roomData = roomDataArrayList.get(i);
                if (roomData.getRoomName().equals(roomName.toLowerCase())) return i;
            }
        }return 0;
    }


    public static void addToButtonDataMap(String roomName, ArrayList<ButtonData> buttonData) {

       Data.buttonDataMap.put(roomName, buttonData);
       Log.d("mqtt",Data.buttonDataMap.get(roomName).get(0).getButtonName());
    }

    public static ArrayList<ButtonData> getButtonDataArrayList(String roomName){
        Log.d("mqtt","Button Data Size "+Data.buttonDataMap.get(roomName).size());
        return Data.buttonDataMap.get(roomName);
    }

    public static void updateButtonState(String topic, MqttMessage message, int index){

        try {
            String roomName = getRoomName(topic);
            String key = getPayloadKey(topic);
            Log.d("mqtt", "Button State before update " + Data.buttonDataMap.get(roomName).get(index).getButtonName()+ " " + Data.buttonDataMap.get(roomName).get(index).getButtonState());
            Data.buttonDataMap.get(roomName).get(index).setButtonState(message.toString());
            Log.d("mqtt", "Button State after update " + Data.buttonDataMap.get(roomName).get(index).getButtonName()+ " " + Data.buttonDataMap.get(roomName).get(index).getButtonState());
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
