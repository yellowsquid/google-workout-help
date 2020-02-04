package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends WearableActivity
        implements MessageClient.OnMessageReceivedListener {
    String msg = "WatchApp";

    private TextView activityText;
    private TextView timeText;
    private ProgressBar pBar;


    private TextView statText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);


        statText = (TextView) findViewById(R.id.statusText) ;


        final Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //statText.setText("Hello World");
                openSportActivity();
            }
        });



    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(msg, "Started MainActivity");
//    }

    public void openSportActivity(){
        Intent intent = new Intent(this, SportActivity.class);
        // Use to pass byte array to sports
        //intent.putExtra("Circuit", )
        startActivity(intent);
    }



    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        //if (messageEvent.getPath().equals(TEST_MESSAGE_PATH)) {
        byte[] data = messageEvent.getData();
        try {
            Object message = Deserializer.deserialize(data);
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Failed to receive message");
        }
        if (message instanceof Circuit) {
            // this.circuit = data;
        } else if (message instanceof StartSignal) {
            // if (this.circuit != null) {
            //     openSportActivity(circuit)
            // }
        }
        // }
    }
}


