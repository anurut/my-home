package com.anurut.customadapter.room;

import com.anurut.customadapter.button.MyListData;

public class RoomData {

    private String roomName;
    private String mqttStatus;
    private MyListData[] buttons;

    public RoomData(String roomName, String mqttStatus, MyListData[] buttons){
        this.roomName = roomName;
        this.mqttStatus = mqttStatus;
        this.buttons = buttons;
    }

    public String getRoomName(){ return roomName;}

    public void setRoomName(String roomName){ this.roomName = roomName;}

    public String getMqttStatus(){ return mqttStatus;}

    public void setMqttStatus(String mqttStatus){ this.mqttStatus = mqttStatus;}

    public MyListData[] getButtons() { return buttons; }

    public void setButtonsData(MyListData[] listData){   this.buttons = listData;   }
}
