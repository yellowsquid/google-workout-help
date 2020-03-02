package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class DevicesLiveData extends LiveData<List<String>> {
    private static final int DEVICE_REFRESH_PERIOD = 10000; // 10 seconds
    private final Timer timer;
    private GetDevicesTask devicesTask;
    private final Application application;

    public DevicesLiveData(Application application) {
        this.application = application;
        timer = new Timer();
        devicesTask = new GetDevicesTask(application, this::postValue);
    }

    @Override
    protected void onActive() {
        // start timer
        timer.schedule(devicesTask, 0, DEVICE_REFRESH_PERIOD);
    }

    @Override
    protected void onInactive() {
        // pause timer
        devicesTask.cancel();
        devicesTask = new GetDevicesTask(application, this::postValue);
    }

    private static class GetDevicesTask extends TimerTask {
        private final Observer<? super List<String>> listener;
        private final Application application;

        GetDevicesTask(Application application, Observer<? super List<String>> listener) {
            this.listener = listener;
            this.application = application;
        }

        public void run() {
            Log.i("DevicesLiveData", "Getting connected devices");
            Wearable.getNodeClient(application).getConnectedNodes().addOnSuccessListener(list -> {
                List<String> deviceNames = list.stream().map(Node::getDisplayName).sorted()
                        .collect(Collectors.toList());
                listener.onChanged(deviceNames);
            });
        }
    }
}