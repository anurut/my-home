package com.anurut.myHome.mqtt;

import android.util.Log;

import com.anurut.myHome.Data;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MqttMessageReceived {

    private MqttMessage message;
    private String topic;
    private JSONObject payload;


    public MqttMessageReceived(String topic, MqttMessage message) {

        try {
            this.message = message;
            this.topic = topic;

            updateButtonState(topic, message.toString());

        } catch (Exception m) {
            m.printStackTrace();
        }
    }


    public void updateButtonState(String topic, String payloadStr) {

        try {
            if (payloadStr.contains("{"))
                payload = new JSONObject(payloadStr);

            if (topic.contains("POWER") || (topic.contains("tele") && topic.contains("LWT"))) {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
