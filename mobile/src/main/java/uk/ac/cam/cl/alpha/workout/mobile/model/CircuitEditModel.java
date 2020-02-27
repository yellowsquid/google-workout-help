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

    public LiveData<String> getName() {
        return repository.getCircuitName(circuitId);
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

    public void deleteExercises(List<Integer> positions) {
        repository.dispatch(repository.deleteExercises(circuitId, positions));
    }

    public void updateLaps(int laps) {
        repository.dispatch(repository.updateLaps(circuitId, laps));
    }

    public void updateName(String name) {
        repository.dispatch(repository.updateName(circuitId, name));
    }

    public void swapExercises(int fromPos, int toPos) {
        repository.dispatch(repository.swapExercises(circuitId, fromPos, toPos));
    }
}
