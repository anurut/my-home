package com.anurut.myHome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anurut.myHome.Data;
import com.anurut.myHome.MainActivity;
import com.anurut.myHome.R;

import static com.anurut.myHome.fragments.SettingsFragmentData.*;

public class SettingsFragment extends Fragment {

    private EditText host;
    private EditText port;
    private EditText username;
    private EditText password;
    private EditText clientId;
    private EditText subTopics;
    private TextView okButton;
    private TextView cancelButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_page, container, false);

        host = view.findViewById(R.id.editText_HostAddress);
        port = view.findViewById(R.id.editText_port);
        username = view.findViewById(R.id.editText_username);
        password = view.findViewById(R.id.editText_password);
        clientId = view.findViewById(R.id.editText_clientId);
        subTopics = view.findViewById(R.id.editText_subTopic);

        okButton = view.findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to save data to shared preferences
                if (!isAnyFieldEmpty()) {
                    setHostURL(host.getText().toString());
                    setPort(Integer.parseInt(port.getText().toString()));
                    setUsername(username.getText().toString());
                    setPassword(password.getText().toString());
                    if (subTopics.getText().toString().isEmpty()) {
                        String[] subTopic = {"stat/#", "tele/#"};
                        setSubTopics(subTopic);
                    } else {
                        String currentString = subTopics.getText().toString();
                        String[] subTopic = currentString.split(",");
                        setSubTopics(subTopic);
                    }

                    Log.d("mqtt", "URL: " + getHostURL() + " Port: " + getPort() + " Username: " + getUsername());
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();

                    MainActivity.mainActivity.changeFragment(new MainPage());
                } else
                    Toast.makeText(getContext(), "Fields can't be left empty", Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton = view.findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.changeFragment(new MainPage());
            }
        });


        return view;
    }

    private boolean isAnyFieldEmpty() {

        if (host.getText().toString().isEmpty() ||
                port.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty()
        ) {
            return true;
        } else return false;

    }


}
