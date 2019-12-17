package com.anurut.myHome.fragments;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anurut.myHome.Data;
import com.anurut.myHome.model.Button;

import java.util.ArrayList;

public class RoomViewModel extends ViewModel {

    private String roomName;
    private static MutableLiveData<ArrayList<Button>> buttonArrayList;
    private static MutableLiveData<String> mqttStatus;

    public MutableLiveData<String> getMqttStatus() {

        if(RoomViewModel.mqttStatus == null){
            RoomViewModel.mqttStatus = new MutableLiveData<>();

            RoomViewModel.mqttStatus.setValue(Data.getMqttStatus());
        }

        return mqttStatus;
    }

    public static void setMqttStatus(String mqttStatus) {

        if(RoomViewModel.mqttStatus == null){
            RoomViewModel.mqttStatus = new MutableLiveData<>();
        }

        RoomViewModel.mqttStatus.setValue(mqttStatus);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public MutableLiveData<ArrayList<Button>> getButtonArrayList() {
        return buttonArrayList;
    }

    public static void setButtonArrayList(ArrayList<Button> buttonArrayList) {

        if(RoomViewModel.buttonArrayList == null){
            RoomViewModel.buttonArrayList = new MutableLiveData<>();
        }

        RoomViewModel.buttonArrayList.setValue(buttonArrayList);
    }

}
