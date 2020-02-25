package uk.ac.cam.cl.alpha.workout.mobile.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

import uk.ac.cam.cl.alpha.workout.mobile.database.AppRepository;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

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

    public LiveData<Integer> getLaps() {
        return repository.getLaps(circuitId);
    }

    public void setCircuitId(long circuitId) {
        this.circuitId = circuitId;
    }

    public Future<Integer> appendExercise(ExerciseType type) {
        return repository.dispatch(repository.appendExercise(circuitId, type));
    }

    public void updateItemDuration(int position, int duration) {
        repository.dispatch(repository.updateExerciseDuration(circuitId, position, duration));
    }
}
