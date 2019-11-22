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

import static com.anurut.customadapter.Data.getRoomDataAttayList;
import static com.anurut.customadapter.Data.getRoomName;

public class MqttMessageReceived {

    private MqttMessage message;
    private String topic;
    // private JSONObject payload;
    public CallResponse callResponse;


    public MqttMessageReceived(String topic, MqttMessage message, CallResponse callResponse) {

        try {
            this.message = message;
            this.topic = topic;
            this.callResponse = callResponse;


            String payload = message.toString();

            if (topic.contains("tele") && payload.toLowerCase().equals("online")) {
                setRooms(topic);

            }
            if (topic.contains("tele") && payload.toLowerCase().equals("offline")) {
                deleteRoom(topic);
            }

            if (topic.contains("stat") && topic.contains("RESULT"))
                setRoomButtons(topic, this.message);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception m) {
            m.printStackTrace();
        }
        //updateButtonState();
    }

    private void setRooms(String topic) {

        String room_name;
        String roomName = getRoomName(topic);
        int roomImageId;

        // set room name and icon
        switch (roomName.toLowerCase()) {
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

        if (!Data.roomAlreadyExist(roomName)) {

            Data.addToRoomDataArrayList(new RoomData(roomName, roomImageId));
            Log.d("mqtt", "Room added: " + roomName);
        }

        callResponse.getResponse(getRoomDataAttayList());
    }

    private void deleteRoom(String topic) {

        String roomName = getRoomName(topic);

        if (Data.roomAlreadyExist(roomName)) {

            Data.deleteRoomDataFromArrayList(roomName);

            Log.d("mqtt", "Room deleted: " + roomName);
            Log.d("mqtt", "Rooms remained: " + getRoomDataAttayList().toString());
        }

        callResponse.getResponse(getRoomDataAttayList());
    }

    private void setRoomButtons(String topic, MqttMessage message) throws JSONException {

        String messageString = message.toString();
        JSONObject payload = new JSONObject();

        if (messageString.contains("{")) {
            String msgPayload = new String(message.getPayload());
            payload = new JSONObject(msgPayload);
        }

        ArrayList<ButtonData> buttonData = new ArrayList<>();
        String roomName = Data.getRoomName(topic);

        if (topic.contains("RESULT") && payload.has("Time")) {
            switch (topic.toLowerCase()) {
                case "stat/masterbedroom/result":

                    if (payload.has("POWER1") && payload.has("Time")) {
                        Log.d("mqtt", "Setting up POWER1");
                        buttonData.add(new ButtonData("tube light", R.drawable.ic_light_bulb_default,R.drawable.ic_light_bulb_on,R.drawable.ic_light_bulb_idle, "cmnd/masterbedroom/POWER1", "stat/masterbedroom/POWER1", roomName, payload.getString("POWER1")));
                    }

                    if (payload.has("POWER2") && payload.has("Time")) {
                        Log.d("mqtt", "Setting up POWER2");
                        buttonData.add(new ButtonData("night light", R.drawable.power_black_24dp,R.drawable.power_yellow_24dp,R.drawable.power_green_24dp, "cmnd/masterbedroom/POWER2", "stat/masterbedroom/POWER2", roomName, payload.getString("POWER2")));
                    }

                    if (payload.has("POWER3") && payload.has("Time")) {
                        Log.d("mqtt", "Setting up POWER3");
                        buttonData.add(new ButtonData("ceiling fan", R.drawable.ic_fan_default,R.drawable.ic_fan_on,R.drawable.ic_fan_idle, "cmnd/masterbedroom/POWER3", "stat/masterbedroom/POWER3", roomName, payload.getString("POWER3")));
                    }

                    if (payload.has("POWER4") && payload.has("Time")) {
                        Log.d("mqtt", "Setting up POWER4");
                        buttonData.add(new ButtonData("dummy light", R.drawable.ic_light_bulb_default,R.drawable.ic_light_bulb_on,R.drawable.ic_light_bulb_idle, "cmnd/masterbedroom/POWER4", "stat/masterbedroom/POWER4", roomName, payload.getString("POWER4")));
                    }
                    break;
                case "stat/kitchen/result":
                    if (payload.has("POWER1")) {
                        //
                    }

                    if (payload.has("POWER2")) {
                        //
                    }

                    if (payload.has("POWER3")) {
                        //
                    }

                    if (payload.has("POWER4")) {
                        //
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + topic.toLowerCase());
            }
            Data.addToButtonDataMap(roomName, buttonData);//aButtonState.setButtonState(roomName, buttonState);
        }


    }

    public void updateButtonState() {

        if (topic.contains("POWER")) {
            switch (topic) {
                case "stat/masterbedroom/POWER1":
                    Data.updateButtonState(topic, message, 0);
                    break;
                case "stat/masterbedroom/POWER2":
                    Data.updateButtonState(topic, message, 1);
                    break;
                case "stat/masterbedroom/POWER3":
                    Data.updateButtonState(topic, message, 2);
                    break;
                case "stat/masterbedroom/POWER4":
                    Data.updateButtonState(topic, message, 3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + topic);
            }
        }


    }

}
