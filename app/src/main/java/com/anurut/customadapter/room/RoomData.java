package com.anurut.customadapter.room;

public class RoomData {

    private String roomName;
    private int roomImageId;

    public RoomData(String roomName, int roomImageId){
        this.roomName = roomName;
        this.roomImageId = roomImageId;
    }


    public String getRoomName(){ return roomName;}

    public void setRoomName(String roomName){ this.roomName = roomName;}

    public int getRoomImageId(){ return roomImageId;}

    public void setRoomImageId(int roomImageId){ this.roomImageId = roomImageId;}

    //public RoomData[] getRooms() { return rooms; }

//    public void setRoomsData(String roomName, int roomImageId){
//       this.roomData.add(new RoomData(roomName,roomImageId));
//    }
//
//    public ArrayList<RoomData> getRoomData(){
//        return this.roomData;
//    }
}
