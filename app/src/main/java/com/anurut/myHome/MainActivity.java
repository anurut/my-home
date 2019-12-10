package com.anurut.myHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;


    public MqttHelper helper;
    public static final String MSG = "com.anurut.myHome.ROOMS";

    public static MainActivity mainActivity;
    public int activityStateCheck = 0;
    public String buttonTagHold = "";
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;


        // Set toolbar here
        toolbar = findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        // Get data from shared preferences
        Data data = new Data();
        SettingsFragmentData.setAllMqttDataFromSharedPrefs(MainActivity.this);


        //Show different fragments w.r.t shared pref values
        if (data.getSharedPreferenceValue(MainActivity.this, "mqtt", getResources().getString(R.string.shared_prefs_key_host)).isEmpty() &&
                data.getSharedPreferenceValue(MainActivity.this, "mqtt", getResources().getString(R.string.shared_prefs_key_config)).isEmpty()) {
            changeFragment(new DefaultFragment());
        } else {

            try {
                Data.setConfig(new JSONObject(data.getSharedPreferenceValue(MainActivity.this, "mqtt", getResources().getString(R.string.shared_prefs_key_config))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            changeFragment(new MainPage());
            startMqtt();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mqtt_settings:
                //do something here
                openMqttSettings();
                break;
            /*case R.id.config_settings:
                //something something
                break;*/

            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return super.onOptionsItemSelected(item);
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

                MqttMessageReceived messageReceived = new MqttMessageReceived(topic, message);

                MainPage.refreshData();
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

    public void openMqttSettings() {

        changeFragment(new SettingsFragment());

    }

    public void saveDefaultJson() throws JSONException {
        String defaultJson = "{\"Room1\":{\"name\":\"master bedroom\",\"type\":\"bedroom\",\"show_image\":true,\"state_topic\":\"tele/masterbedroom/STATE\",\"lwt_topic\":\"tele/masterbedroom/LWT\",\"Button1\":{\"name\":\"tube light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER1\",\"state_topic\":\"stat/masterbedroom/POWER1\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\"},\"Button2\":{\"name\":\"night light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER2\",\"state_topic\":\"stat/masterbedroom/POWER2\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\"},\"Button3\":{\"name\":\"ceiling fan\",\"type\":\"fan\",\"command_topic\":\"cmnd/masterbedroom/POWER3\",\"state_topic\":\"stat/masterbedroom/POWER3\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\"},\"Button4\":{\"name\":\"dummy light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER4\",\"state_topic\":\"stat/masterbedroom/POWER4\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\"}}}";

        JSONObject jsonObject = new JSONObject(defaultJson);
        ExternalStorageUtil.saveJsonConfigFile(MainActivity.this, "config.json", jsonObject);
    }


}
