package com.anurut.myHome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;


import com.anurut.myHome.button.ButtonData;
import com.anurut.myHome.room.RoomData;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    private static ArrayList<RoomData> roomDataArrayList = new ArrayList<>();
    private static HashMap<String, ArrayList<ButtonData>> buttonDataMap = new HashMap<>();
    private static String mqttStatus;
    private static JSONObject config;
    public static ArrayList<RoomData> getRoomDataAttayList() {
        return roomDataArrayList;
    }

    public static JSONObject getConfig() {
        return config;
    }

    public static void setConfig(JSONObject config) {
        Data.config = config;
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
        Data.roomDataArrayList.remove(Data.getRoomNameIndex(roomName));
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


    //TODO: Continue from here ...
    public String roomNameFromJson(String topic) throws JSONException {

        JSONObject jsonObject = Data.getConfig();

        for(int i=0; i<jsonObject.length(); i++){
            JSONObject roomJson = jsonObject.getJSONObject("Room"+(i+1));
            String roomName = roomJson.getString("name");

            for(int j=0; j<roomJson.length(); j++){
                JSONObject buttonJson = roomJson.getJSONObject("Button"+(j+1));
                String buttonTopic = buttonJson.getString("state_topic");
                if(buttonTopic.equalsIgnoreCase(topic)){
                    Log.d("mqtt", "Room Name : " + roomName);
                    return roomName;
                }
            }
        }
        return "";
    }

    public static String getPayloadKey(String topic) {

        String currentString = topic;
        String[] separated = currentString.split("/");
        String payloadKey = separated[2];
        return payloadKey;
    }


    public void setupRooms(String config) throws JSONException {
        JSONObject configJson = new JSONObject(config);
        int jsonLength = configJson.length();

        for(int i=0; i<jsonLength; i++){
            setupRoomsData(configJson.getJSONObject("Room"+(i+1)));
        }
    }


    //Takes input from config.json and sets up Room Icons
    public void setupRoomsData(JSONObject room) throws JSONException {

        int roomImageId = 0;
        int numberOfButtons = room.length();
        ArrayList<ButtonData> buttonData = new ArrayList<>();
        String roomName = room.getString("name");

        // set room name and icon
        switch (room.getString("type")) {
            case "bedroom":
                if(room.getBoolean("show_image")){
                    roomImageId = R.drawable.bedroom_img;
                } else {
                    roomImageId = R.drawable.bedroom_black_24dp;
                }
                break;
            case "bathroom":
                if(room.getBoolean("show_image")){
                    //roomImageId = R.drawable.bathroom_black_24dp;
                }else {
                    roomImageId = R.drawable.bathroom_black_24dp;
                }
                break;
            case "kitchen":
                if(room.getBoolean("show_image")){
                    roomImageId = R.drawable.kitchen_img;
                } else {
                    roomImageId = R.drawable.kitchen_black_24dp;
                }
                break;
            default:
                throw new IllegalStateException("This room type does not exist: " + room.getString("type").toLowerCase());
        }

        if (!Data.roomAlreadyExist(roomName)) {
            Data.addToRoomDataArrayList(new RoomData(roomName,roomImageId,room.getString("type"), room.getString("state_topic"), room.getString("lwt_topic")));
            Log.d("mqtt", "Room added: " + roomName);
        }

        for(int i=0; i<numberOfButtons; i++){
            buttonData.add(setupRoomButtons(room.getJSONObject("Button"+(i+1)), roomName));
        }

        Data.addToButtonDataMap(roomName, buttonData);//aButtonState.setButtonState(roomName, buttonState);
        View contextView = MainActivity.mainActivity.findViewById(R.id.mainActivity);
        Snackbar snackbar = Snackbar.make(contextView,"Rooms setup successfully!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private ButtonData setupRoomButtons(JSONObject buttons, String roomName) throws JSONException {

        String buttonName = buttons.getString("name");
        int defaultImgId = 0;
        int imageIdStateOn = 0;
        int imageIdStateIdle = 0;


        if(!buttons.getString("type").isEmpty() || !(buttons.getString("type") == null)) {
            switch (buttons.getString("type").toLowerCase()){
                case "light":
                    defaultImgId = R.drawable.ic_light_bulb_default;
                    imageIdStateOn = R.drawable.ic_light_bulb_on;
                    imageIdStateIdle = R.drawable.ic_light_bulb_idle;
                    break;
                case "fan":
                    defaultImgId = R.drawable.ic_fan_default;
                    imageIdStateOn = R.drawable.ic_fan_on;
                    imageIdStateIdle = R.drawable.ic_fan_idle;
                    break;
                case "night light":
                    defaultImgId = R.drawable.ic_night_lamp_default;
                    imageIdStateOn = R.drawable.ic_night_lamp_on;
                    imageIdStateIdle = R.drawable.ic_night_lamp_idle;
                    break;
                default:
                    defaultImgId = R.drawable.power_black_24dp;
                    imageIdStateOn = R.drawable.power_yellow_24dp;
                    imageIdStateIdle = R.drawable.power_green_24dp;
            }
        }

        return new ButtonData(buttonName, buttons.getString("type"),defaultImgId,imageIdStateOn,imageIdStateIdle,
                buttons.getString("command_topic"),buttons.getString("state_topic"),roomName,buttons.getString("payload_on"),
                buttons.getString("payload_off"),"");
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
                throw new Exception("Could not get index of button topic: " + topic);
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
