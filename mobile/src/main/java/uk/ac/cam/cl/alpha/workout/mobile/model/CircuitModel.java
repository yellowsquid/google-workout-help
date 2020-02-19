package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import uk.ac.cam.cl.alpha.workout.mobile.database.AppRepository;
import uk.ac.cam.cl.alpha.workout.mobile.database.Task;
import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class CircuitModel extends AndroidViewModel {
    private final AppRepository repository;

    public CircuitModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public LiveData<List<Circuit>> getCircuits() {
        return repository.getCircuits();
    }

    public <V> Future<V> dispatch(Callable<V> task) {
        return repository.dispatch(task);
    }

    public Task<Long> createCircuit(BareCircuit circuit) {
        return repository.createCircuit(circuit);
    }

    public LiveData<Circuit> getCircuit(long id) {
        return repository.getCircuit(id);
    }
}
