package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import uk.ac.cam.cl.alpha.workout.mobile.database.AppRepository;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class ServerModel extends AndroidViewModel {
    private final AppRepository repository;
    private final Application application;
    private final Observer<Circuit> circuitObserver;

    public long getCircuitId() {
        return circuitId;
    }

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

    public void sendPauseSignal() {
        new SendTask(application, Signal.PAUSE, Constants.SIGNAL_PATH).execute();
    }

    public void sendResumeSignal() {
        new SendTask(application, Signal.RESUME, Constants.SIGNAL_PATH).execute();
    }

    public void sendStopSignal() {
        new SendTask(application, Signal.STOP, Constants.SIGNAL_PATH).execute();
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
}
