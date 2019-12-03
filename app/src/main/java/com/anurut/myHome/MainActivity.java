package com.anurut.myHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anurut.myHome.Interface.CallResponse;
import com.anurut.myHome.fragments.DefaultFragment;
import com.anurut.myHome.fragments.MainPage;
import com.anurut.myHome.fragments.SettingsFragment;
import com.anurut.myHome.fragments.SettingsFragmentData;
import com.anurut.myHome.helper.MqttHelper;
import com.anurut.myHome.helper.MqttMessageReceived;
import com.anurut.myHome.room.RoomData;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.anurut.myHome.ExternalStorageUtil.readJsonFromFile;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;


    public MqttHelper helper;
    public static final String MSG = "com.anurut.myHome.ROOMS";

    public static MainActivity mainActivity;
    public int activityStateCheck = 0;
    public String buttonTagHold = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        Data data = new Data();

        SettingsFragmentData.setAllMqttDataFromSharedPrefs(MainActivity.this);

        if (data.getSharedPreferenceValue(MainActivity.this, "mqtt", "host").isEmpty()) {
            changeFragment(new DefaultFragment());
        } else {
            changeFragment(new MainPage());
            startMqtt();
        }

        readJsonFromFile("config.json","config");


        //TODO: remove this from here and move it to a class
        // Check whether this app has write external storage permission or not.
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // If do not grant write external storage permission.
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else {
            try {
                saveDefaultJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public void startMqtt() {

        //mqttConnectionStatus = findViewById(R.id.mqttStatus);

        helper = new MqttHelper(getApplicationContext());
        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.w("mqtt", "Reconnected to : " + serverURI);
                    Data.setMqttStatus("connected");
                    syncButtonStates();
                } else {
                    Log.w("mqtt: ", "Connected to - ClientID: " + helper.mqttAndroidClient.getClientId() + " " + serverURI);
                    Data.setMqttStatus("connected");
                    syncButtonStates();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

                Log.w("Connection Lost : ", cause);
                Data.setMqttStatus("disconnected");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msgPayload = new String(message.getPayload());
                Log.d("mqtt", "Message arrived!, Topic: " + topic + " Payload: " + msgPayload);

                MqttMessageReceived messageReceived = new MqttMessageReceived(topic, message, new CallResponse() {
                    @Override
                    public void getResponse(ArrayList<RoomData> roomData) {

                        System.out.println("Row Count : " + roomData.size());

                        /*RoomAdapter adapter = new RoomAdapter(MainActivity.this, roomData);
                        if (roomData.size() <= 2)
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                        else
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        recyclerView.setAdapter(adapter);*/

                        MainPage.refreshData();
                    }
                });

                try {
                    messageReceived.updateButtonState();
                } catch (Exception e) {
                    Log.d("mqtt", "" + e);
                }


                if (MainActivity.mainActivity.activityStateCheck == 1)
                    RoomActivity.roomActivity.refreshData(buttonTagHold);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("mqtt", "Delivery complete" + token.toString());
            }
        });
    }

    public void syncButtonStates() {

        String publishTopic = "cmnd/sonoffs/state";
        String payload = "";

        helper.publishMessage(payload, publishTopic);
    }


    // This method is called in onClick listener of buttonAdapter
    public void publish(String publishTopic, String currentState) {

        if (currentState.equalsIgnoreCase("ON")) {
            helper.publishMessage("OFF", publishTopic);
        } else {
            helper.publishMessage("ON", publishTopic);
        }
    }

    public void openSettings(View view) {

        changeFragment(new SettingsFragment());

    }

    public void saveDefaultJson() throws JSONException {
        String defaultJson = "{\"Room1\":{\"name\":\"master bedroom\",\"Button1\":{\"name\":\"tube light\",\"command topic\":\"cmnd/master bedroom/POWER1\"}}}";

        JSONObject jsonObject = new JSONObject(defaultJson);
        ExternalStorageUtil.saveJsonConfigFile(MainActivity.this,"config.json", jsonObject);
    }


}
