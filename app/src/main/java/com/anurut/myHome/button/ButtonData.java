package com.anurut.myHome.button;

import java.util.ArrayList;

public class ButtonData {

    private String buttonName;
    private int defaultImageId;
    private int imageIdStateOn;
    private int imageIdStateIdle;
    private String buttonState;
    private String commandTopic;
    //private String mqttStatus;
    private String stateTopic;
    private String roomName;
    private String type;
    private String payloadON;
    private String payloadOFF;
    private String lwtTopic;
    private String lwt_available;
    private String lwt_unavailable;
    private static ArrayList<ButtonData> buttonDataArrayList = new ArrayList<>();


    public ButtonData(String buttonName, String type, int defaultImageId, int imageIdStateOn, int imageIdStateIdle,
                      String commandTopic, String stateTopic, String roomName , String payloadON, String payloadOFF ,
                      String buttonState, String lwtTopic, String lwt_available, String lwt_unavailable){

        this.buttonName = buttonName;
        this.defaultImageId = defaultImageId;
        this.imageIdStateOn = imageIdStateOn;
        this.imageIdStateIdle = imageIdStateIdle;
        this.commandTopic = commandTopic;
        this.stateTopic = stateTopic;
        this.roomName = roomName;
        this.buttonState = buttonState;
        this.type = type;
        this.payloadOFF = payloadOFF;
        this.payloadON = payloadON;
        this.lwtTopic = lwtTopic;
        this.lwt_available = lwt_available;
        this.lwt_unavailable = lwt_unavailable;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public static ArrayList<ButtonData> getButtonDataArrayList() {
        return buttonDataArrayList;
    }

    public static void setButtonDataArrayList(ArrayList<ButtonData> buttonDataArrayList) {
        ButtonData.buttonDataArrayList = buttonDataArrayList;
    }

    public String getPayloadON() {
        return payloadON;
    }

    public void setPayloadON(String payloadON) {
        this.payloadON = payloadON;
    }

    public String getPayloadOFF() {
        return payloadOFF;
    }

    public void setPayloadOFF(String payloadOFF) {
        this.payloadOFF = payloadOFF;
    }

    public String getStateTopic() {   return stateTopic;   }

    public void setStateTopic(String stateTopic) {  this.stateTopic = stateTopic;   }

    public int getDefaultImageId() {
        return defaultImageId;
    }

    public int getImageIdStateOn() { return imageIdStateOn; }

    public int getImageIdStateIdle() {
        return imageIdStateIdle;
    }

    public void setDefaultImageId(int defaultImageId) {
        this.defaultImageId = defaultImageId;
    }

    public String getButtonState() {   return buttonState;    }

    public void setButtonState(String buttonState) {
        this.buttonState = buttonState;
    }

    public String getButtonName(){    return this.buttonName;    }

    public void setButtonName(String buttonName){ this.buttonName = buttonName;}

    public String getCommandTopic(){return this.commandTopic;}

    public void setCommandTopic(String commandTopic){ this.commandTopic = commandTopic;}

//    public String getMqttStatus(){return this.mqttStatus;}
//
//    public void setMqttStatus(String mqttStatus){ this.mqttStatus = mqttStatus;}

}
