package uk.ac.cam.cl.alpha.workout.mobile.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public Task<Integer> appendExercise(long circuitId, ExerciseType type) {
        return new Task<>(() -> {
            ExerciseDao dao = database.getExerciseDao();
            int position = dao.countExercises(circuitId);
            Exercise exercise = Exercise.create(circuitId, position, type);
            dao.insertExercise(exercise);
            return position;
        });
    }

    public LiveData<List<Exercise>> getExercises(long circuitId) {
        return database.getExerciseDao().getExercises(circuitId);
    }

    public List<Exercise> getExercisesNow(long circuitId) {
        return database.getExerciseDao().getExercisesNow(circuitId);
    }

    public <V> Future<V> dispatch(Callable<V> task) {
        return executor.submit(task);
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

    public Task<?> updateExerciseDuration(long circuitId, int position, int duration) {
        return new Task<>(() -> {
            ExerciseDao exerciseDao = database.getExerciseDao();
            ExerciseType type = exerciseDao.getType(circuitId, position);
            Exercise exercise = Exercise.create(circuitId, position, duration, type);
            exerciseDao.updateExercise(exercise);
        });
    }

    public Task<?> deleteExercises(List<Exercise> toDelete, long circuitID) {
        return new Task<>(() -> {
            List<Exercise> currentList = database.getExerciseDao().getExercisesNow(circuitID);
            List<Exercise> newList = new ArrayList<>();
            for(Exercise e1 : currentList) {
                boolean keep = true;
                for(Exercise e2 : toDelete) {
                    if(e1.getPosition() == e2.getPosition()) {
                        Log.d("Deleting position", "" + e2.getPosition());
                        keep = false;
                        break;
                    }
                }

                if(keep) {
                    newList.add(e1);
                }
            }

            final int size = newList.size();
            for(int i = 0; i < size; i++) {
                Exercise old = newList.get(i);
                newList.set(i, Exercise.create(circuitID,  i, old.getDuration(), old.getExerciseType()));
            }

            database.getExerciseDao().deleteExercises(currentList);
            database.getExerciseDao().insertExercises(newList);
        });
    }

    public Task<?> deleteCircuits(List<Circuit> toDelete){
        return new Task<>(() -> {
            for (Circuit circuit : toDelete) {
                database.getCircuitDao().deleteCircuit(circuit.getCircuit());
            }
        });
    }

    public Task<?> updateLaps(long circuitId, int laps) {
        return new Task<>(() -> {
            CircuitDao circuitDao = database.getCircuitDao();
            int oldLaps = circuitDao.getLapsNow(circuitId);

            if (laps == oldLaps) {
                return;
            }

            String name = circuitDao.getNameNow(circuitId);
            BareCircuit circuit = BareCircuit.create(circuitId, name, laps);
            circuitDao.updateCircuit(circuit);
        });
    }

    public Task<?> updateName(long circuitId, String name) {
        return new Task<>(() -> {
            CircuitDao circuitDao = database.getCircuitDao();
            String oldName = circuitDao.getNameNow(circuitId);

            if (name.equals(oldName)) {
                return;
            }

            int laps = circuitDao.getLapsNow(circuitId);
            BareCircuit circuit = BareCircuit.create(circuitId, name, laps);
            circuitDao.updateCircuit(circuit);
        });
    }

    public Task<?> swapExercises(long circuitId, int fromPos, int toPos) {
        return new Task<>(() -> {
            ExerciseDao exerciseDao = database.getExerciseDao();
            Exercise fromExercise = exerciseDao.getExerciseNow(circuitId, fromPos);
            Exercise toExercise = exerciseDao.getExerciseNow(circuitId, toPos);

            Exercise newFromExercise = Exercise.create(circuitId, fromPos, toExercise.getDuration(),
                                                       toExercise.getExerciseType());
            Exercise newToExercise = Exercise.create(circuitId, toPos, fromExercise.getDuration(),
                                                     fromExercise.getExerciseType());
            exerciseDao.updateExercises(newFromExercise, newToExercise);
        });
    }
}
