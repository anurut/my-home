package com.anurut.myHome.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anurut.myHome.Data;
import com.anurut.myHome.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DefaultFragment extends Fragment {
    private static final int SAVE_JSON = 1;
    private static final int CREATE_JSON = 2;
    private static final int READ_JSON = 3;

    Button saveConfig;
    Button createFile;
    Button readConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default, container, false);

        saveConfig = view.findViewById(R.id.save_config);
        createFile = view.findViewById(R.id.create_json);
        readConfig = view.findViewById(R.id.read_config);

        createFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                intent.putExtra(Intent.EXTRA_TITLE, "config.json");

                startActivityForResult(intent, CREATE_JSON);
            }
        });

        saveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");

                startActivityForResult(intent, SAVE_JSON);
            }
        });

        readConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");

                startActivityForResult(intent, READ_JSON);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_JSON) {
                if (data != null) {
                    writeFileContent(data.getData());
                    Log.d("json", "JSON file updated");
                }
            }
            if (requestCode == CREATE_JSON) {
                Log.d("json", "JSON file created");
            }
            if (requestCode == READ_JSON) {
                if (data != null) {
                    try {
                        new Data().saveSharedPreferences(getActivity(), "mqtt", getResources().getString(R.string.shared_prefs_key_config), readFileContent(data.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("json", "JSON file read");
                }
            }
        }
    }

    private void writeFileContent(Uri uri) {
        try {
            ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(uri, "w");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            pfd.getFileDescriptor());

            String textContent = "{\"Room1\":{\"name\":\"master bedroom\",\"type\":\"bedroom\",\"show_image\":true,\"Button1\":{\"name\":\"tube light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER1\",\"state_topic\":\"stat/masterbedroom/POWER1\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\",\"lwt_topic\":\"tele/masterbedroom/LWT\",\"lwt_available\":\"Online\",\"lwt_unavailable\":\"Offline\"},\"Button2\":{\"name\":\"night light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER2\",\"state_topic\":\"stat/masterbedroom/POWER2\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\",\"lwt_topic\":\"tele/masterbedroom/LWT\",\"lwt_available\":\"Online\",\"lwt_unavailable\":\"Offline\"},\"Button3\":{\"name\":\"ceiling fan\",\"type\":\"fan\",\"command_topic\":\"cmnd/masterbedroom/POWER3\",\"state_topic\":\"stat/masterbedroom/POWER3\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\",\"lwt_topic\":\"tele/masterbedroom/LWT\",\"lwt_available\":\"Online\",\"lwt_unavailable\":\"Offline\"},\"Button4\":{\"name\":\"dummy light\",\"type\":\"light\",\"command_topic\":\"cmnd/masterbedroom/POWER4\",\"state_topic\":\"stat/masterbedroom/POWER4\",\"payload_on\":\"ON\",\"payload_off\":\"OFF\",\"lwt_topic\":\"tele/masterbedroom/LWT\",\"lwt_available\":\"Online\",\"lwt_unavailable\":\"Offline\"}}}";

            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "File does not exist", Toast.LENGTH_SHORT);
        } catch (IOException f) {
            f.printStackTrace();
            Toast.makeText(getContext(), "File could not be read", Toast.LENGTH_SHORT);
        } catch (NullPointerException g) {
            g.printStackTrace();
            Toast.makeText(getContext(), "Oops!", Toast.LENGTH_SHORT);
        }
    }

    private String readFileContent(Uri uri) throws IOException {

        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
