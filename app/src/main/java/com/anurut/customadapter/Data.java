package com.anurut.customadapter;

import com.anurut.customadapter.button.ButtonData;
import com.anurut.customadapter.room.RoomData;

import java.util.ArrayList;

public class Data {

    private static ArrayList<ButtonData> buttonDataArrayList = new ArrayList<>();
    private static ArrayList<RoomData> roomDataArrayList = new ArrayList<>();

    public static boolean buttonState;
    public static boolean roomState;

    public static ArrayList<RoomData> getRoomDataAttayList(){  return roomDataArrayList;    }

    // Returns true if room name already exists in roomDataArrayList
    public static boolean roomAlreadyExist(String roomName){

        RoomData roomData;

        if(!(roomDataArrayList.size() == 0)){
            for(int i=0; i<roomDataArrayList.size(); i++){
                roomData = roomDataArrayList.get(i);
                if(roomData.getRoomName().equals(roomName.toLowerCase())) return true;
            }
        }
        return false;
    }


    public static void addToRoomDataArrayList(RoomData roomData) {
        Data.roomDataArrayList.add(roomData);
    }

    public static void deleteRoomDataFromArrayList(String roomName) {
        Data.roomDataArrayList.remove(new Data().getValueIndex(roomName));
    }

    public static String getRoomName(String topic){
        String roomName;
        String currentString = topic;
        String[] separated = currentString.split("/");
        roomName =  separated[1];
        return roomName;
    }


    int getValueIndex(String roomName) {

        RoomData roomData;

        if (!(roomDataArrayList.size() == 0)) {
            for (int i = 0; i < roomDataArrayList.size(); i++) {
                roomData = roomDataArrayList.get(i);
                if (roomData.getRoomName().equals(roomName.toLowerCase())) return i;
            }
        }return 0;
    }
}
