package com.anurut.myHome.fragments;

import android.app.Activity;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;

public class SettingsFragmentData {

    private static String hostURL;
    private static String port;
    private static String username;
    private static String password;
    private static String subTopics;
    private static String clientId;

    public static String getClientId() {  return clientId;   }

    public static void setClientId(String clientId) {  SettingsFragmentData.clientId = clientId;  }

    public static String getHostURL() {
        return hostURL;
    }

    public static void setHostURL(String hostURL) {
        SettingsFragmentData.hostURL = hostURL;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        SettingsFragmentData.port = port;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SettingsFragmentData.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SettingsFragmentData.password = password;
    }

    public static String getSubTopics() {
        return subTopics;
    }

    public static void setSubTopics(String subTopics) {
        SettingsFragmentData.subTopics = subTopics;
    }

    public static void setAllMqttDataFromSharedPrefs(Activity activity){

        Data data = new Data();

            SettingsFragmentData.hostURL = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_host));
            SettingsFragmentData.port = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_port));
            SettingsFragmentData.username = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_username));
            SettingsFragmentData.password = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_password));
            SettingsFragmentData.subTopics = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_subTopics));
            SettingsFragmentData.clientId = data.getSharedPreferenceValue(activity,"mqtt",activity.getResources().getString(R.string.shared_prefs_key_clientid));
    }
}
