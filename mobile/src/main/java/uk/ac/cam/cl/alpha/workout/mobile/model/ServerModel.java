package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import uk.ac.cam.cl.alpha.workout.mobile.database.AppRepository;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.Serializer;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class ServerModel extends AndroidViewModel {
    private final AppRepository repository;
    private final Application application;
    private final Observer<Circuit> circuitObserver;
    private long circuitId;
    private LiveData<? extends Circuit> data;
    private final LiveData<List<String>> devicesList;

    public ServerModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = AppRepository.getInstance(application);
        circuitObserver =
                circuit -> new SendTask(application, circuit, Constants.CIRCUIT_PATH).execute();
        devicesList = new DevicesLiveData(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (data != null) {
            data.removeObserver(circuitObserver);
        }
    }

    public void sendStartSignal() {
        new SendTask(application, Signal.START, Constants.SIGNAL_PATH).execute();
    }

    public LiveData<String> getCircuitName() {
        return repository.getCircuitName(circuitId);
    }

    public LiveData<List<String>> getDevicesList() {
        return devicesList;
    }

    public void setCircuitId(long circuitId) {
        if (data != null) {
            data.removeObserver(circuitObserver);
        }

        this.circuitId = circuitId;
        data = repository.getCircuit(circuitId);
        data.observeForever(circuitObserver);
    }

    private static class SendTask extends AsyncTask<Void, Void, Void> {
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
}
