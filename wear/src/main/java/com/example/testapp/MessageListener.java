package com.example.testapp;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;

public class MessageListener implements MessageClient.OnMessageReceivedListener {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //if (messageEvent.getPath().equals(TEST_MESSAGE_PATH)) {
            String testMessage = messageEvent.getData().toString();
            TextView t = (TextView) findViewById(R.id.text);
            Toast.makeText(getActivity().getApplicationContext(), "text", Toast.LENGTH_LONG).show();
        //}
    }


}