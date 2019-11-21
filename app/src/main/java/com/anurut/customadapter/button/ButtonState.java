package com.anurut.customadapter.button;

import com.anurut.customadapter.Data;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.anurut.customadapter.Data.*;

public class ButtonState {

    final static int sdk = android.os.Build.VERSION.SDK_INT;

    // Key <topic>, Value <payload>
    private static HashMap<String, ArrayList<String>> buttonState = new HashMap<>();

    public static String getButtonState(String roomName, int position){
        return ButtonState.buttonState.get(roomName).get(position);
    }

    public static void setButtonState(String roomName, ArrayList<String> buttonState){

        ButtonState.buttonState.put(roomName, buttonState);
    }

}
