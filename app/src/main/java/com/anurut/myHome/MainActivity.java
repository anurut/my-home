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

import com.anurut.myHome.fragments.DefaultFragment;
import com.anurut.myHome.fragments.MainFragment;
import com.anurut.myHome.fragments.MainViewModel;
import com.anurut.myHome.fragments.SettingsFragment;
import com.anurut.myHome.fragments.SettingsFragmentData;
import com.anurut.myHome.mqtt.MqttHelper;
import com.anurut.myHome.mqtt.MqttMessageReceived;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public MqttHelper helper;

    public static MainActivity mainActivity;
    public Toolbar toolbar;


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
                Data.setConfig(new JSONObject(
                                data.getSharedPreferenceValue(MainActivity.this, "mqtt", getResources().getString(R.string.shared_prefs_key_config))
                        )
                );

                for (int i = 0; i < Data.getConfig().length(); i++) {
                    data.setupRoomsData(Data.getConfig().getJSONObject("Room" + (i + 1)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            changeFragment(new MainFragment());
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

    public void changeFragment(Fragment fragment, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("data",data);

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public void startMqtt() {

        helper = new MqttHelper(getApplicationContext());
        helper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.w("mqtt", "Reconnected to : " + serverURI);
                    Data.setMqttStatus("connected");
                    MainViewModel.setMqttStatus("connected");
                    //MainPage.refreshMqttStatus();
                    syncButtonStates();
                } else {
                    Log.w("mqtt: ", "Connected to - ClientID: " + helper.mqttAndroidClient.getClientId() + " " + serverURI);
                    Data.setMqttStatus("connected");
                   MainViewModel.setMqttStatus("connected");
                    //MainPage.refreshMqttStatus();
                    syncButtonStates();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

                Log.w("Connection Lost : ", cause);
                Data.setMqttStatus("disconnected");
                MainViewModel.setMqttStatus(Data.getMqttStatus());
                //MainPage.refreshMqttStatus();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msgPayload = new String(message.getPayload());
                Log.d("mqtt", "Message arrived!, Topic: " + topic + " Payload: " + msgPayload);

                new MqttMessageReceived(topic, message);

                //MainPage.refreshData();

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

        changeFragment(new SettingsFragment(), "SettingsFragment");

    }
}
