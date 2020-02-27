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
    private long circuitId;

    public CircuitModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public LiveData<List<Circuit>> getCircuits() {
        return repository.getCircuits();
    }

    public long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(long circuitId) {
        this.circuitId = circuitId;
    }

    public Task<Long> createCircuit(BareCircuit circuit) {
        return repository.createCircuit(circuit);
    }

    public void deleteCircuits(Iterable<Long> positions){
        List<Circuit> toDelete = new ArrayList<>();
        for (long position: positions) {
            toDelete.add(getCircuit(position).getValue());
        }
        repository.dispatch(repository.deleteCircuits(toDelete));
    }

    public LiveData<Circuit> getCircuit(long id) {
        return repository.getCircuit(id);
    }

    public boolean isCircuitSelected() {
        return circuitId != 0;
    }

    public void dispatch(Task<Long> task) {
        repository.dispatch(task);
    }
}
