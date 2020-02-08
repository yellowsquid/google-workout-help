package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Serializer;
import com.example.testapp.shared.Signal;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends WearableActivity implements
        MessageClient.OnMessageReceivedListener {
    private byte[] circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        final TextView statText = findViewById(R.id.statusText);
        // Enables Always-on
        setAmbientEnabled();

        final Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                openSportActivity(null);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(msg, "Started MainActivity");
//    }



    public void openSportActivity(byte[] circuit){

        Intent intent = new Intent(this, SportActivity.class);
        intent.putExtra("Circuit", circuit);
        // Use to pass byte array to sports
        //intent.putExtra("Circuit", )
        startActivity(intent);

    }



    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        statText.setText("A");
        if (messageEvent.getPath().equals("/circuit_path_name")) {
            byte[] data = messageEvent.getData();
            try {
                Object message = Serializer.deserialize(data);

                if (message instanceof Circuit) {
                    this.circuit = data;
                    statText.setText("Circuit received");
                    // TODO: Change status message to "Circuit received"
                } else if (message instanceof Signal && ((Signal) message).getMessage()
                        .equals("START")) {
                    if (this.circuit != null) {
                        //openSportActivity(circuit);
                        statText.setText("Starting Circuit");
                    } else {
                        statText.setText("Starting Signal received");
                        System.err.println("Start signal received before circuit received");
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                System.err.println("Failed to receive message");
            }
        }
    }
}


