package com.anurut.myHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.anurut.myHome.Interface.CallResponse;
import com.anurut.myHome.helper.MqttHelper;
import com.anurut.myHome.helper.MqttMessageReceived;
import com.anurut.myHome.room.RoomAdapter;
import com.anurut.myHome.room.RoomData;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public MqttHelper helper;
    public RecyclerView recyclerView;
    public static String MSG = "com.anurut.myHome.ROOMS";

    public static MainActivity mainActivity;
    public int activityStateCheck  = 0;
    public String buttonTagHold = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        startMqtt();

        recyclerView = findViewById(R.id.roomsRecyclerView);
        recyclerView.setHasFixedSize(true);

    }

    public void startMqtt(){

        //mqttConnectionStatus = findViewById(R.id.mqttStatus);

        helper = new MqttHelper(getApplicationContext());
        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.w("mqtt","Reconnected to : "+ serverURI);
                    Data.setMqttStatus("connected");
                    if (activityStateCheck == 1)
                        RoomActivity.roomActivity.refreshData();
                } else {
                    Log.w("mqtt: ","Connected to - ClientID: "+ helper.mqttAndroidClient.getClientId()+" "+serverURI);
                    Data.setMqttStatus("connected");
                    if (activityStateCheck == 1)
                        RoomActivity.roomActivity.refreshData();
                    syncButtonStates();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

                Log.w("Connection Lost : ",cause);
                Data.setMqttStatus("disconnected");
                if (activityStateCheck == 1)
                    RoomActivity.roomActivity.refreshData();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msgPayload = new String(message.getPayload());
                Log.w("mqtt","Message arrived!, Topic: "+ topic+ " Payload: " + msgPayload);

                MqttMessageReceived messageReceived= new MqttMessageReceived(topic, message, new CallResponse() {
                    @Override
                    public void getResponse(ArrayList<RoomData> roomData) {

                        System.out.println("Row Count : "+  roomData.size());

                        RoomAdapter adapter = new RoomAdapter(MainActivity.this,roomData);
                        if(roomData.size() <= 2)
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                        else
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        recyclerView.setAdapter(adapter);

                    }
                });

                try{
                    messageReceived.updateButtonState();
                } catch (Exception e){
                    Log.d("mqtt", "" + e);
                }

                if (activityStateCheck == 1)
                    RoomActivity.roomActivity.refreshData();


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
    }


    // This method is called in onClick listener of buttonAdapter
    public void publish(String publishTopic, String currentState){

        if(currentState.equalsIgnoreCase("ON")){
            helper.publishMessage("OFF",publishTopic);
        }else{
            helper.publishMessage("ON",publishTopic);
        }
    }

}
