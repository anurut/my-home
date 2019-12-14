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
import java.util.Objects;

public class Data {

    private static ArrayList<RoomData> roomDataArrayList = new ArrayList<>();
    private static HashMap<String, ArrayList<ButtonData>> buttonDataMap = new HashMap<>();
    private static String mqttStatus;
    private static JSONObject config;

    public static ArrayList<RoomData> getRoomDataAttayList() {  return roomDataArrayList;  }

    public static void setRoomDataArrayList(ArrayList<RoomData> roomDataArrayList) { Data.roomDataArrayList = roomDataArrayList;  }

    static JSONObject getConfig() { return config;  }

    static void setConfig(JSONObject config) {   Data.config = config;  }

    public static String getMqttStatus() { return mqttStatus;  }

    static void setMqttStatus(String mqttStatus) {  Data.mqttStatus = mqttStatus;   }

    // Returns true if room name already exists in roomDataArrayList
    private static boolean roomAlreadyExist(String roomName) {

        RoomData roomData;

        if (!(roomDataArrayList.size() == 0)) {
            for (int i = 0; i < roomDataArrayList.size(); i++) {
                roomData = roomDataArrayList.get(i);
                if (roomData.getRoomName().equals(roomName.toLowerCase())) return true;
            }
        }
        return false;
    }

    private static void addToRoomDataArrayList(RoomData roomData) {  Data.roomDataArrayList.add(roomData);   }

    //Takes input from config.json and sets up Room Icons
    void setupRoomsData(JSONObject room) throws JSONException {

        int roomImageId = 0;

        int numberOfButtons = room.length();
        ArrayList<ButtonData> buttonData = new ArrayList<>();
        String roomName = room.getString("name");

        // set room name and icon
        switch (room.getString("type")) {
            case "bedroom":
                if (room.getBoolean("show_image")) {
                    roomImageId = R.drawable.bedroom_img;
                } else {
                    roomImageId = R.drawable.bedroom_black_24dp;
                }
                break;
            case "bathroom":
                if (room.getBoolean("show_image")) {
                    //roomImageId = R.drawable.bathroom_black_24dp;
                } else {
                    roomImageId = R.drawable.bathroom_black_24dp;
                }
                break;
            case "kitchen":
                if (room.getBoolean("show_image")) {
                    roomImageId = R.drawable.kitchen_img;
                } else {
                    roomImageId = R.drawable.kitchen_black_24dp;
                }
                break;
            default:
                throw new IllegalStateException("This room type does not exist: " + room.getString("type").toLowerCase());
        }

        if (!Data.roomAlreadyExist(roomName)) {
            Data.addToRoomDataArrayList(new RoomData(roomName, roomImageId, room.getString("type")));
            Log.d("mqtt", "Room added: " + roomName);
        }

        for (int i = 0; i < numberOfButtons; i++) {
            if (room.has("Button" + (i + 1)))
                buttonData.add(setupRoomButtons(room.getJSONObject("Button" + (i + 1)), roomName));
        }

        Data.addToButtonDataMap(roomName, buttonData);//aButtonState.setButtonState(roomName, buttonState);
        View contextView = MainActivity.mainActivity.findViewById(R.id.mainActivity);
        Snackbar snackbar = Snackbar.make(contextView, "Rooms setup successfully!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private ButtonData setupRoomButtons(JSONObject buttons, String roomName) throws JSONException {

        String buttonName = buttons.getString("name");
        int defaultImgId ;
        int imageIdStateOn ;
        int imageIdStateIdle ;

        switch (buttons.getString("type").toLowerCase()) {
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
            case "water heater":
                defaultImgId = R.drawable.ic_water_heater_default;
                imageIdStateOn = R.drawable.ic_water_heater_on;
                imageIdStateIdle = R.drawable.ic_water_heater_idle;
                break;
            default:
                defaultImgId = R.drawable.power_black_24dp;
                imageIdStateOn = R.drawable.power_yellow_24dp;
                imageIdStateIdle = R.drawable.power_green_24dp;
        }


        return new ButtonData(buttonName, buttons.getString("type"), defaultImgId, imageIdStateOn, imageIdStateIdle,
                buttons.getString("command_topic"), buttons.getString("state_topic"), roomName, buttons.getString("payload_on"),
                buttons.getString("payload_off"), "",buttons.getString("lwt_topic"),buttons.getString("lwt_available"),
                buttons.getString("lwt_unavailable"));
    }

    private static void addToButtonDataMap(String roomName, ArrayList<ButtonData> buttonData) {

        Data.buttonDataMap.put(roomName, buttonData);
    }

    static ArrayList<ButtonData> getButtonDataArrayList(String roomName) {
        Log.d("mqtt", "Button Data Size " + Data.buttonDataMap.get(roomName).size());
        return Data.buttonDataMap.get(roomName);
    }

    public static void updateButtonState(String topic, MqttMessage message) {


        try{

            JSONObject jsonObject = Data.getConfig();

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject roomJson = jsonObject.getJSONObject("Room" + (i + 1));
                String roomName = roomJson.getString("name");

                for (int j = 0; j < roomJson.length(); j++) {

                    JSONObject buttonJson = roomJson.getJSONObject("Button" + (j + 1));
                    String buttonStateTopic = buttonJson.getString("state_topic");
                    String lwtTopic = buttonJson.getString("lwt_topic");

                    if (topic.equalsIgnoreCase(buttonStateTopic) || topic.equalsIgnoreCase(lwtTopic)) {
                        Log.d("mqtt", "Button State before update " + Objects.requireNonNull(Data.buttonDataMap.get(roomName)).get(j).getButtonName() + " " +
                                Data.buttonDataMap.get(roomName).get(j).getButtonState());

                        Data.buttonDataMap.get(roomName).get(j).setButtonState(message.toString());

                        Log.d("mqtt", "Button State after update " + Objects.requireNonNull(Data.buttonDataMap.get(roomName)).get(j).getButtonName() + " " +
                                Data.buttonDataMap.get(roomName).get(j).getButtonState());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Save shared preference to device memory
    public void saveSharedPreferences(Activity activity, String fileName, String key, String value) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    //Fetch shared preference from device memory
    public String getSharedPreferenceValue(Activity activity, String fileName, String key) {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }

}
