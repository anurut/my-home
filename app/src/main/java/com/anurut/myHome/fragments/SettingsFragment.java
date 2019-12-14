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

        Data data = new Data();
        if(!data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_host)).isEmpty()){
            host.setText(data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_host)));
            port.setText(data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_port)));
            username.setText(data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_username)));
            clientId.setText(data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_clientid)));
            subTopics.setText(data.getSharedPreferenceValue(getActivity(),"mqtt",getResources().getString(R.string.shared_prefs_key_subTopics)));
        }

        okButton = view.findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data = new Data();

                // Code to save data to shared preferences
                if (!isAnyFieldEmpty()) {

                    setHostURL(host.getText().toString());
                    data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_host), getHostURL());
                    setPort(port.getText().toString());
                    data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_port), getPort());
                    setUsername(username.getText().toString());
                    data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_username), getUsername());
                    setPassword(password.getText().toString());
                    data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_password), getPassword());
                    setClientId(clientId.getText().toString());
                    data.saveSharedPreferences(getActivity(),"mqtt", getResources().getString(R.string.shared_prefs_key_clientid), getClientId());

                    if (subTopics.getText().toString().isEmpty()) {
                        String subTopic = "stat/#,tele/#";
                        setSubTopics(subTopic);
                        data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_subTopics), getSubTopics());
                    } else {

                        String subTopic = subTopics.getText().toString();
                        setSubTopics(subTopic);
                        data.saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_subTopics), getSubTopics());
                    }

                    Log.d("mqtt", "URL: " + getHostURL() + " Port: " + getPort() + " Username: " + data.getSharedPreferenceValue(getActivity(),"mqtt",getString(R.string.shared_prefs_key_username)));

                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();

                    //Restart activities
                    Intent i = new Intent(getContext(), MainActivity.class);
                    // set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                } else
                    Toast.makeText(getContext(), "Fields can't be left empty", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton = view.findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data = new Data();

                if (data.getSharedPreferenceValue(getActivity(), "mqtt", getString(R.string.shared_prefs_key_host)).isEmpty()) {
                    MainActivity.mainActivity.changeFragment(new DefaultFragment());
                } else {
                    MainActivity.mainActivity.changeFragment(new MainPage());
                }
            }
        });
        return view;
    }

    private boolean isAnyFieldEmpty() {

        if (host.getText().toString().isEmpty() ||
                port.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() ||
                clientId.getText().toString().isEmpty()
        ) {
            return true;
        } else return false;

    }


}
