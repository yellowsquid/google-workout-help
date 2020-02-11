package com.example.testapp.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Serializer;
import com.example.testapp.shared.Signal;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.Serializable;

public class ServerModel extends ViewModel {
    private static final String PATH = "/circuit_path_name";

    public void setCircuit(Context context, Circuit circuit) {
        new SendTask(context, circuit).execute();
    }

    public void sendStartSignal(Context context) {
        new SendTask(context, Signal.START).execute();
    }

    private static class SendTask extends AsyncTask<Void, Void, Void> {
        private static final String LOG_TAG = "SendTask";
        private final Context context;
        private final Serializable data;

        SendTask(Context context, Serializable data) {
            this.context = context;
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                MessageClient messageClient = Wearable.getMessageClient(context);
                byte[] bytes = Serializer.serialize(data);
                Wearable.getNodeClient(context).getConnectedNodes().addOnSuccessListener(list -> {
                    for (Node node : list) {
                        messageClient.sendMessage(node.getId(), PATH, bytes)
                                .addOnCompleteListener(task -> {
                                    String name = node.getDisplayName();

                                    if (task.isSuccessful()) {
                                        Log.d(LOG_TAG, "Sent message to " + name);
                                    } else {
                                        Log.w(LOG_TAG, "Failed to message " + name);
                                    }
                                });
                    }
                });
                Log.d(LOG_TAG, "Dispatched data send");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to serialise circuit.");
            }

            return null;
        }
    }
}
