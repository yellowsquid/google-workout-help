package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

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

    public Task<Long> createCircuit(BareCircuit circuit) {
        return repository.createCircuit(circuit);
    }

    public void deleteCircuits(Iterable<Long> positions) {
        List<BareCircuit> circuits = new ArrayList<>();

        for (long id : positions) {
            circuits.add(BareCircuit.create(id, "", 1));
        }

        repository.dispatch(repository.deleteCircuits(circuits));
    }

    public void dispatch(Runnable task) {
        repository.dispatch(task);
    }
}
