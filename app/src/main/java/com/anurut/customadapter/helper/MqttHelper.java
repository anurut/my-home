package com.anurut.customadapter.helper;


import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MqttHelper {

    public MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://malhan.duckdns.org:1883";//"tcp://192.168.1.99:1883";
    String clientID = MqttClient.generateClientId();//"AndroidClient";
    final String[] subTopic = {"stat/#","tele/#"};//"stat/#";
    final int[] qos = {1,1};
    final String userName = "admin";
    final String password = "bhootbangla";

    // Constructor
    public MqttHelper(Context context) {

        mqttAndroidClient = new MqttAndroidClient(context,serverUri, clientID);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.w("mqtt", serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("mqtt","Connection Lost: " + cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.w("mqtt", message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.w("mqtt", "Delivery complete: "+token.toString());
            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback){
        mqttAndroidClient.setCallback(callback);
    }

    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());

        try{
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                    //subscribe to topics below
                    subscribeToTopics();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("mqtt", "Failed to connect to: " + serverUri + " " + exception.toString());
                }
            });
        } catch (MqttException m){
            m.printStackTrace();
        }

    }

    public void disconnect(){

        try{
            mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt","Disconnected!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("mqtt","Failed to disconnect!");
                }
            });
        } catch (MqttException mx){
            mx.printStackTrace();
        }
    }

    public void subscribeToTopics(){

            try{
                mqttAndroidClient.subscribe(subTopic, qos, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("mqtt","Subscribed! " + subTopic);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("mqtt","Subscription Failed!");
                    }
                });
            } catch (MqttException m){
                m.printStackTrace();
            }
    }

    public void publishMessage(String publishMessage, String publishTopic) {

            try {
                MqttMessage pMessage = new MqttMessage();
                pMessage.setPayload(publishMessage.getBytes());
                mqttAndroidClient.publish(publishTopic, pMessage);
            } catch (Exception mq) {
                mq.printStackTrace();
            }
        }

}
