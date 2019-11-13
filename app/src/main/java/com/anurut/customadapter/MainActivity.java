package com.anurut.customadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anurut.customadapter.Interface.CallResponse;
import com.anurut.customadapter.helper.MqttHelper;
import com.anurut.customadapter.helper.MqttMessageReceived;
import com.anurut.customadapter.room.RoomAdapter;
import com.anurut.customadapter.room.RoomData;
import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public MqttHelper helper;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.roomsRecyclerView);
        recyclerView.setHasFixedSize(true);



        startMqtt();

    }

    public void startMqtt(){

        //mqttConnectionStatus = findViewById(R.id.mqttStatus);

        helper = new MqttHelper(getApplicationContext());
        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.w("mqtt","Reconnected to : "+ serverURI);
                    //mqttConnectionStatus.setText("Connected Again!");
                    //syncButtonStates();


                } else {
                    Log.w("mqtt: ","Connected to - ClientID: "+ helper.mqttAndroidClient.getClientId()+" "+serverURI);
                    //mqttConnectionStatus.setText("Connected");
                    syncButtonStates();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

                Log.w("Connection Lost : ",cause);
                //mqttConnectionStatus.setText("Disconnected");
                //setButtonStateDisabled();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msgPayload = new String(message.getPayload());
                Log.w("mqtt","Message arrived!, Topic: "+ topic+ " Payload: " + msgPayload);
                MqttMessageReceived messageReceived= new MqttMessageReceived(topic, message, new CallResponse() {
                    @Override
                    public void getResponse(ArrayList<RoomData> roomData) {

                        System.out.println("Row Count : "+  roomData.size());

                        RoomAdapter adapter = new RoomAdapter(roomData);
                        if(roomData.size() <= 2)
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                        else
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        recyclerView.setAdapter(adapter);

                    }
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("mqtt", "Delivery complete" + token.toString());
            }
        });
    }

    public void syncButtonStates(){

        String publishTopic = "cmnd/sonoffs/state";
        String payload = "";

        helper.publishMessage(payload,publishTopic);

        View contextView = findViewById(R.id.mainActivity);
        Snackbar snackbar = Snackbar.make(contextView,"Button state synced successfully!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
