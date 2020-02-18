package uk.ac.cam.cl.alpha.workout.mobile.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        database = Room.databaseBuilder(application, AppDatabase.class, "app_database").build();
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

    public Task<Integer> appendExercise(long circuitId, ExerciseType type, int duration) {
        return new Task<>(() -> {
            ExerciseDao dao = database.getExerciseDao();
            int position = dao.countExercises(circuitId);
            Exercise exercise = Exercise.create(circuitId, duration, position, type);
            dao.insertExercise(exercise);
            return position;
        });
    }

    public LiveData<List<Exercise>> getExercises(long circuitId) {
        return database.getExerciseDao().getExercises(circuitId);
    }

    public <V> Future<V> dispatch(Callable<V> task) {
        return executor.submit(task);
    }

    public LiveData<Circuit> getCircuit(long id) {
        return database.getCircuitDao().getCircuit(id);
    }
}
