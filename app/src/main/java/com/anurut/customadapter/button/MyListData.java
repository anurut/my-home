package com.anurut.customadapter.button;

import java.util.ArrayList;

public class MyListData {

    private String buttonName;
    private int buttonId;
    private ArrayList<MyListData> data;

    public MyListData(String buttonName, int buttonImgId){
        this.buttonName = buttonName;
        this.buttonId = buttonImgId;
    }

    public String getButtonName(){    return buttonName;    }

    public void setButtonName(String buttonName){ this.buttonName = buttonName;}

    public int getButtonImgId(){ return buttonId;}

    public void setButtonImgId(int buttonId){ this.buttonId = buttonId;}

    public ArrayList<MyListData> getListData(){ return data; }

    public void setListData(String buttonName, int buttonImageId){

        this.data.add(new MyListData(buttonName, buttonImageId));

    }


}
