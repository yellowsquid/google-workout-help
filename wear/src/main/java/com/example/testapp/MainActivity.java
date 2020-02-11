package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Serializer;
import com.example.testapp.shared.Signal;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;

public class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener {
    TextView statText;
    private byte[] circuit;

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        statText.setText("Received Message");
        if (messageEvent.getPath().equals("/circuit_path_name")) {
            byte[] data = messageEvent.getData();
            try {
                Object message = Serializer.deserialize(data);

                if (message instanceof Circuit) {
                    circuit = data;
                    statText.setText("Circuit received");
                    // TODO: Change status message to "Circuit received"
                } else if ((message) == Signal.START) {
                    if (circuit != null) {
                        openSportActivity(circuit);
                        statText.setText("Starting Circuit");
                    } else {
                        statText.setText("Starting Signal received");
                        System.err.println("Start signal received before circuit received");
                    }
                } else {
                    statText.setText("Unknown message");
                }
            } catch (ClassNotFoundException | IOException e) {
                System.err.println("Failed to receive message");
            }
        }
    }

    //    @Override
    //    protected void onStart() {
    //        super.onStart();
    //        Log.d(msg, "Started MainActivity");
    //    }

    public void openSportActivity(byte[] circuit) {

        Intent intent = new Intent(this, SportActivity.class);
        intent.putExtra("Circuit", circuit);
        // Use to pass byte array to sports
        //intent.putExtra("Circuit", )
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        Wearable.getMessageClient(this).addListener(this);
        statText = findViewById(R.id.statusText);
        statText.setText("Awaiting workout");
        // Enables Always-on
        setAmbientEnabled();

        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openSportActivity(null);
            }
        });
    }
}


