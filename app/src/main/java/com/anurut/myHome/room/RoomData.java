package com.anurut.myHome.room;

public class RoomData {

    private String roomName;
    private int roomImageId;
    private String type;
    private boolean showImage;
    private String stateTopic;
    private String lwtTopic;


    public RoomData(String roomName, int roomImageId, String type, String stateTopic, String lwtTopic) {
        this.roomName = roomName;
        this.roomImageId = roomImageId;
        this.type = type;
        //this.showImage = showImage;
        this.stateTopic = stateTopic;
        this.lwtTopic = lwtTopic;
    }

    public String getStateTopic() {
        return stateTopic;
    }

    public void setStateTopic(String stateTopic) {
        this.stateTopic = stateTopic;
    }

    public String getLwtTopic() {
        return lwtTopic;
    }

    public void setLwtTopic(String lwtTopic) {
        this.lwtTopic = lwtTopic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomImageId() {
        return roomImageId;
    }

    public void setRoomImageId(int roomImageId) {
        this.roomImageId = roomImageId;
    }

}
