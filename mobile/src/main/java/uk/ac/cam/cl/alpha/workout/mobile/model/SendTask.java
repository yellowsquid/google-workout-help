package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.Serializable;

import uk.ac.cam.cl.alpha.workout.shared.Serializer;

public class SendTask extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = "SendTask";
    private final String path;
    private final Application application;
    private final Serializable data;

    SendTask(Application application, Serializable data, String path) {
        this.path = path;
        this.application = application;
        this.data = data;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            MessageClient messageClient = Wearable.getMessageClient(application);
            byte[] bytes = Serializer.serialize(data);
            Wearable.getNodeClient(application).getConnectedNodes()
                    .addOnSuccessListener(list -> {
                        for (Node node : list) {
                            messageClient.sendMessage(node.getId(), path, bytes)
                                    .addOnCompleteListener(task -> {
                                        String name = node.getDisplayName();

                                        if (task.isSuccessful()) {
                                            Log.d(LOG_TAG,
                                                  String.format("Sent message to %s", name));
                                        } else {
                                            Log.w(LOG_TAG,
                                                  String.format("Failed to message %s", name));
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
