package com.anurut.myHome.helper;

import android.util.Log;

import com.anurut.myHome.Data;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.anurut.myHome.Data.getRoomDataAttayList;
import static com.anurut.myHome.Data.getRoomNameFromJson;

public class MqttMessageReceived {

    private MqttMessage message;
    private String topic;
    private JSONObject payload;
    //public CallResponse callResponse;


    public MqttMessageReceived(String topic, MqttMessage message) {

        try {
            this.message = message;
            this.topic = topic;

            String payloadStr = message.toString();


            if (topic.contains("tele") && payloadStr.toLowerCase().equals("online")) {
                //TODO: enable room icon w.r.t "topic"
                //setRooms(topic);

            }
            if (topic.contains("tele") && payloadStr.toLowerCase().equals("offline")) {
                //TODO: Disable room icon w.r.t "topic"
                //deleteRoom(topic);
            }

            updateButtonState(topic, payloadStr);

            /*if (topic.contains("stat") && topic.contains("RESULT"))
                setRoomButtons(topic, this.message);*/

        } catch (Exception m) {
            m.printStackTrace();
        }
    }

    private void setRooms(String topic) {

/*        String room_name;
        String roomName = getRoomName(topic);
        int roomImageId;

        // set room name and icon
        switch (roomName.toLowerCase()) {
            case "master bedroom":
                roomImageId = R.drawable.bedroom_img;

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
                roomImageId = R.drawable.kitchen_img;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + roomName.toLowerCase());
        }

        if (!Data.roomAlreadyExist(roomName)) {
            Data.addToRoomDataArrayList(new RoomData(roomName, roomImageId));
            Log.d("mqtt", "Room added: " + roomName);
        }

        //callResponse.getResponse(getRoomDataAttayList());*/
    }


    private void deleteRoom(String topic) throws JSONException {

        String roomName = getRoomNameFromJson(topic);

        if (Data.roomAlreadyExist(roomName)) {

            Data.deleteRoomDataFromArrayList(roomName);

            Log.d("mqtt", "Room deleted: " + roomName);
            Log.d("mqtt", "Rooms remained: " + getRoomDataAttayList().toString());
        }

        //callResponse.getResponse(getRoomDataAttayList());
    }

    private void setRoomButtons(String topic, MqttMessage message) throws JSONException {

      /*  String messageString = message.toString();
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
                        buttonData.add(new ButtonData("tube light", "cmnd/masterbedroom/POWER1", "stat/masterbedroom/POWER1", roomName, payload.getString("POWER1")));
                    }

                    if (payload.has("POWER2") && payload.has("Time")) {
                        Log.d("mqtt", "Setting up POWER2");
                        buttonData.add(new ButtonData("night light", R.drawable.ic_night_lamp_default,R.drawable.ic_night_lamp_on,R.drawable.ic_night_lamp_idle, "cmnd/masterbedroom/POWER2", "stat/masterbedroom/POWER2", roomName, payload.getString("POWER2")));
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
                case "stat/kitchen_img/result":
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
            View contextView = MainActivity.mainActivity.findViewById(R.id.mainActivity);
            Snackbar snackbar = Snackbar.make(contextView,"Rooms setup successfully!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/
    }


    public void updateButtonState(String topic, String payloadStr) throws JSONException {

        if (payloadStr.contains("{"))
            payload = new JSONObject(payloadStr);

        if (topic.contains("POWER")) {
            Data.updateButtonState(topic, message);
        }

        if (topic.contains("stat") && topic.contains("RESULT") && payload.has("Time")) {
            Iterator iterator = payload.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Log.d("Key : ", key);
                if (key.contains("POWER")) {
                    String newTopic = topic.replace("RESULT", key);
                    MqttMessage pMessage = new MqttMessage();
                    pMessage.setPayload(payload.getString(key).getBytes());
                    Data.updateButtonState(newTopic, pMessage);
                }
            }
        }
    }

}
