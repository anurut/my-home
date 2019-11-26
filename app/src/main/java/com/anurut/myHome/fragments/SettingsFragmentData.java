package com.anurut.myHome.fragments;

public class SettingsFragmentData {

    public static String hostURL;
    public static int port;
    public static String username;
    public static String password;
    public static String[] subTopics;

    public static String getHostURL() {
        return hostURL;
    }

    public static void setHostURL(String hostURL) {
        SettingsFragmentData.hostURL = hostURL;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
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

    public static String[] getSubTopics() {
        return subTopics;
    }

    public static void setSubTopics(String[] subTopics) {
        SettingsFragmentData.subTopics = subTopics;
    }
}
