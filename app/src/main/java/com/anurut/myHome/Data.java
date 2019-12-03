package com.anurut.myHome;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anurut.myHome.button.ButtonData;
import com.anurut.myHome.room.RoomData;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    private static ArrayList<RoomData> roomDataArrayList = new ArrayList<>();
    private static HashMap<String, ArrayList<ButtonData>> buttonDataMap = new HashMap<>();
    private static String mqttStatus;


    public static ArrayList<RoomData> getRoomDataAttayList() {
        return roomDataArrayList;
    }

    static String getMqttStatus() {
        return mqttStatus;
    }

    static void setMqttStatus(String mqttStatus) {
        Data.mqttStatus = mqttStatus;
    }

    // Returns true if room name already exists in roomDataArrayList
    public static boolean roomAlreadyExist(String roomName) {

        RoomData roomData;

        if (!(roomDataArrayList.size() == 0)) {
            for (int i = 0; i < roomDataArrayList.size(); i++) {
                roomData = roomDataArrayList.get(i);
                if (roomData.getRoomName().equals(roomName.toLowerCase())) return true;
            }
        }
        return false;
    }


    public static void addToRoomDataArrayList(RoomData roomData) {
        Data.roomDataArrayList.add(roomData);
    }

    public static void deleteRoomDataFromArrayList(String roomName) {
        Data.roomDataArrayList.remove( Data.getRoomNameIndex(roomName));
    }

    public static String getRoomName(String topic) {
        // TODO: change below code to take room name from user
        //Initial code, now changed to take room name from user
        String roomName;
        String[] separated = topic.split("/");
        String room_name = separated[1];

        switch (room_name) {
            case "masterbedroom":
                roomName = "master bedroom";
                break;
            case "masterbathroom":
                roomName = "master bathroom";
                break;
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

    public static String getPayloadKey(String topic) {

        String currentString = topic;
        String[] separated = currentString.split("/");
        String payloadKey = separated[2];
        return payloadKey;
    }


    private static int getRoomNameIndex(String roomName) {

        RoomData roomData;

        if (!(roomDataArrayList.size() == 0)) {
            for (int i = 0; i < roomDataArrayList.size(); i++) {
                roomData = roomDataArrayList.get(i);
                if (roomData.getRoomName().equals(roomName.toLowerCase())) return i;
            }
        }
        return 0;
    }

    private static int getRoomButtonIndex(String topic) {

        ButtonData buttonData;
        String roomName = getRoomName(topic);
        ArrayList<ButtonData> buttonDataArrayList = getButtonDataArrayList(roomName);

        if (!(buttonDataArrayList.size() == 0)) {
            for (int i = 0; i < buttonDataArrayList.size(); i++) {
                buttonData = buttonDataArrayList.get(i);
                if (buttonData.getStateTopic().equalsIgnoreCase(topic)) return i;
            }
        }

        return -1;
    }


    public static void addToButtonDataMap(String roomName, ArrayList<ButtonData> buttonData) {

        Data.buttonDataMap.put(roomName, buttonData);
        Log.d("mqtt", Data.buttonDataMap.get(roomName).get(0).getButtonName());
    }

    public static ArrayList<ButtonData> getButtonDataArrayList(String roomName) {
        Log.d("mqtt", "Button Data Size " + Data.buttonDataMap.get(roomName).size());
        return Data.buttonDataMap.get(roomName);
    }

    public static void updateButtonState(String topic, MqttMessage message) {

        try {
            String roomName = getRoomName(topic);
            int index = getRoomButtonIndex(topic);
            //String key = getPayloadKey(topic);

            if (index != -1) {
                Log.d("mqtt", "Button State before update " + Data.buttonDataMap.get(roomName).get(index).getButtonName() + " " + Data.buttonDataMap.get(roomName).get(index).getButtonState());
                Data.buttonDataMap.get(roomName).get(index).setButtonState(message.toString());
                Log.d("mqtt", "Button State after update " + Data.buttonDataMap.get(roomName).get(index).getButtonName() + " " + Data.buttonDataMap.get(roomName).get(index).getButtonState());
            } else {
                throw new Exception("Could not get index of button topic");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveSharedPreferences(Activity activity, String fileName, String key, String value) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public String getSharedPreferenceValue(Activity activity, String fileName, String key) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }

}
