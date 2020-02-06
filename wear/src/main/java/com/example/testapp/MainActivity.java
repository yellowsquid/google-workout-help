package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Serializer;
import com.example.testapp.shared.Signal;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;

public class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener {
    private byte[] circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        TextView statText = findViewById(R.id.statusText);
        // Enables Always-on
        setAmbientEnabled();

        Button button = findViewById(R.id.startButton);
        button.setOnClickListener((v) -> {
            //statText.setText("Hello World");
            //openSportActivity();
        });
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/circuit_path_name")) {
            byte[] data = messageEvent.getData();
            Object message = Serializer.deserialize(data);

            // TODO: make this more object oriented?
            if (message instanceof Circuit) {
                circuit = data;
                // TODO: Change status message to "Circuit received"
            } else if (message instanceof Signal && ((Signal) message).getMessage()
                    .equals("START")) {
                if (circuit != null) {
                    openSportActivity(circuit);
                } else {
                    System.err.println("Start signal received before circuit received");
                }
            }
        }
    }

    private void openSportActivity(byte[] circuit) {
        Intent intent = new Intent(this, SportActivity.class);
        // Use to pass byte array to sports
        //intent.putExtra("Circuit", )
        startActivity(intent);
    }
}


