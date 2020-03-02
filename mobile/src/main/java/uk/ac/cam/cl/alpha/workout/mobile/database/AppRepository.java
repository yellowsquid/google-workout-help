package uk.ac.cam.cl.alpha.workout.mobile.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public final class AppRepository {
    private static final Object LOCK = new Object();
    private static AppRepository instance;
    private final ExecutorService executor;
    private final AppDatabase database;

    private AppRepository(Application application) {
        database = Room.databaseBuilder(application, AppDatabase.class, "app_database")
                .createFromAsset("database/app.db").build();
        executor = Executors.newCachedThreadPool();
    }

    public static AppRepository getInstance(Application application) {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new AppRepository(application);
            }

            //noinspection StaticVariableUsedBeforeInitialization
            return instance;
        }
    }

    public LiveData<List<Circuit>> getCircuits() {
        return database.getCircuitDao().getCircuits();
    }

    public Task<Long> createCircuit(BareCircuit circuit) {
        return new Task<>(() -> database.getCircuitDao().insertCircuit(circuit));
    }

    public Runnable appendExercise(long circuitId, ExerciseType type) {
        return () -> database.getExerciseDao().appendExercise(circuitId, type);
    }

    public LiveData<List<Exercise>> getExercises(long circuitId) {
        return database.getExerciseDao().getExercises(circuitId);
    }

    public void dispatch(Runnable task) {
        executor.submit(task);
    }

    public LiveData<Circuit> getCircuit(long id) {
        return database.getCircuitDao().getCircuit(id);
    }

    public LiveData<Integer> getLaps(long circuitId) {
        return database.getCircuitDao().getLaps(circuitId);
    }

    public LiveData<String> getCircuitName(long circuitId) {
        return database.getCircuitDao().getName(circuitId);
    }

    public Runnable updateExerciseDuration(long circuitId, int position, int duration) {
        return () -> database.getExerciseDao().updateDuration(circuitId, position, duration);
    }

    public Runnable deleteExercises(long circuitID, List<Integer> positions) {
        return () -> database.getExerciseDao().deleteExercises(circuitID, positions);
    }

    public Runnable deleteCircuits(List<BareCircuit> circuits) {
        return () -> database.getCircuitDao().deleteCircuits(circuits);
    }

    public Runnable updateLaps(long circuitId, int laps) {
        return () -> database.getCircuitDao().updateLaps(circuitId, laps);
    }

    public Runnable updateName(long circuitId, String name) {
        return () -> database.getCircuitDao().updateName(circuitId, name);
    }

    public Runnable swapExercises(long circuitId, int fromPos, int toPos) {
        return () -> database.getExerciseDao().swapExercises(circuitId, fromPos, toPos);
    }
}
