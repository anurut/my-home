package com.anurut.customadapter.button;

import java.util.ArrayList;

public class ButtonData {

    private String buttonName;
    private int buttonId;
    private String buttonState;
    private String commandTopic;
    private String stateTopic;
    private String roomName;


    private ArrayList<ButtonData> data;

    public ButtonData(String buttonName, int buttonImgId, String commandTopic, String stateTopic, String roomName , String buttonState){
        this.buttonName = buttonName;
        this.buttonId = buttonImgId;
        this.commandTopic = commandTopic;
        this.stateTopic = stateTopic;
        this.roomName = roomName;
        this.buttonState = buttonState;
    }


    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    public String getButtonState() {   return buttonState;    }

    public void setButtonState(String buttonState) {
        this.buttonState = buttonState;
    }

    public String getButtonName(){    return buttonName;    }

    public void setButtonName(String buttonName){ this.buttonName = buttonName;}

    public int getButtonImgId(){ return buttonId;}

    public void setButtonImgId(int buttonId){ this.buttonId = buttonId;}

    public String getCommandTopic(){return commandTopic;}

}
