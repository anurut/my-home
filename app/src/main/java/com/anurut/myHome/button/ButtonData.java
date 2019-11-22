package com.anurut.myHome.button;

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

    public ButtonData(String buttonName, int defaultImageId, int imageIdStateOn,int imageIdStateIdle, String commandTopic, String stateTopic, String roomName , String buttonState){
        this.buttonName = buttonName;
        this.defaultImageId = defaultImageId;
        this.imageIdStateOn = imageIdStateOn;
        this.imageIdStateIdle = imageIdStateIdle;
        this.commandTopic = commandTopic;
        this.stateTopic = stateTopic;
        this.roomName = roomName;
        this.buttonState = buttonState;
        //this.mqttStatus = mqttStatus;
    }


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
