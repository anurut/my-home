package com.anurut.myHome.fragments;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anurut.myHome.Data;
import com.anurut.myHome.model.Room;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Room>> roomDataArraylist = new MutableLiveData<>();
    private static MutableLiveData<String> mqttStatus;

    public MutableLiveData<ArrayList<Room>> getRoomDataArraylist() {
        return roomDataArraylist;
    }

    public void setRoomDataArraylist(ArrayList<Room> dataArrayList) {
        this.roomDataArraylist.setValue(dataArrayList);
    }

    public MutableLiveData<String> getMqttStatus() {

        if(mqttStatus == null) {
            mqttStatus = new MutableLiveData<>();
            getStatus();
        }

        return mqttStatus;
    }

    public static void setMqttStatus(String mqttStatuss) {
        //Log.d("mqtt", this.mqttStatus.getValue());
        if(mqttStatus == null){
            mqttStatus = new MutableLiveData<>();
            mqttStatus.setValue(mqttStatuss);
            String mqtt = mqttStatus.getValue();
            Log.d("mqtt", mqtt);
        } else {
            mqttStatus.setValue(mqttStatuss);
            String mqtt = mqttStatus.getValue();
            Log.d("mqtt", mqtt);
        }

    }

    void getStatus(){

        String status;
        status = Data.getMqttStatus();
        mqttStatus.setValue(status);
    }

}
