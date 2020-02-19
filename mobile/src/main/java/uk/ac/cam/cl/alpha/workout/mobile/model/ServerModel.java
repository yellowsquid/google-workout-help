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
    private static final int DEVICE_REFRESH_PERIOD = 10000; // 10 seconds
    private final AppRepository repository;
    private final Application application;
    private final Observer<Circuit> circuitObserver;
    private final Timer timer;
    private LiveData<? extends Circuit> data;
    private GetDevicesTask devicesTask;

    public ServerModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = AppRepository.getInstance(application);
        circuitObserver =
                circuit -> new SendTask(application, circuit, Constants.CIRCUIT_PATH).execute();
        timer = new Timer();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (data != null) {
            data.removeObserver(circuitObserver);
        }

        if (devicesTask != null) {
            devicesTask.cancel();
        }
    }

    public void sendStartSignal() {
        new SendTask(application, Signal.START, Constants.SIGNAL_PATH).execute();
    }

    public void setCircuitId(long circuitId) {
        if (data != null) {
            data.removeObserver(circuitObserver);
        }

        data = repository.getCircuit(circuitId);
        data.observeForever(circuitObserver);
    }

    public void setDeviceObserver(Observer<? super List<String>> observer) {
        // TODO: could make task update a LiveData that it return instead of taking observer
        // TODO: could make the timer lifecycle aware: (see androidx lifecycle)
        //  (currently runs in background constantly)
        if (devicesTask != null) {
            devicesTask.cancel();
        }

        devicesTask = new GetDevicesTask(application, observer);
        timer.schedule(devicesTask, 0, DEVICE_REFRESH_PERIOD);
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

    private static class GetDevicesTask extends TimerTask {
        private final Observer<? super List<String>> listener;
        private final Application application;

        GetDevicesTask(Application application, Observer<? super List<String>> listener) {
            this.listener = listener;
            this.application = application;
        }

        public void run() {
            Log.i("ServerModel", "Getting connected devices");
            Wearable.getNodeClient(application).getConnectedNodes().addOnSuccessListener(list -> {
                List<String> deviceNames = list.stream().map(Node::getDisplayName).sorted()
                        .collect(Collectors.toList());
                listener.onChanged(deviceNames);
            });
        }
    }
}
