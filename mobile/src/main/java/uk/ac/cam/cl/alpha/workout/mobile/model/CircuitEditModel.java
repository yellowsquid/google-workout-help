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
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

public class CircuitEditModel extends AndroidViewModel {
    private final AppRepository repository;
    private long circuitId;

    public CircuitEditModel(@NonNull Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public LiveData<List<Exercise>> getExercises() {
        return repository.getExercises(circuitId);
    }

    public void setCircuit(PureCircuit circuit) {
        circuitId = circuit.getId();
    }

    public Task<Integer> appendExercise(ExerciseType type) {
        return repository.appendExercise(circuitId, type);
    }

    public Future<Integer> dispatch(Callable<Integer> task) {
        return repository.dispatch(task);
    }
}
