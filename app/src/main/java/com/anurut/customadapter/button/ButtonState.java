package com.anurut.customadapter.button;

import java.util.HashMap;

public class ButtonState {

    final static int sdk = android.os.Build.VERSION.SDK_INT;

    // Key <topic>, Value <payload>
    private static HashMap<String, String> buttonState = new HashMap<>();

    public static String getButtonState(String buttonName){
        return ButtonState.buttonState.get(buttonName);
    }

    public static void setButtonState(String buttonTopic, String buttonState){
            ButtonState.buttonState.put(buttonTopic, buttonState);
    }

}
