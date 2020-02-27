package uk.ac.cam.cl.alpha.workout.mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

@Dao
interface ExerciseDao {
    @Insert
    void insertExercise(Exercise exercise);

    @Insert
    void insertExercises(List<Exercise> exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercises(List<Exercise> exercises);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId ORDER BY position")
    LiveData<List<Exercise>> getExercises(long circuitId);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId ORDER BY position")
    List<Exercise> getExercisesNow(long circuitId);

    @Query("SELECT count(*) FROM exercises WHERE circuit_id = :circuitId")
    int countExercises(long circuitId);

    @Query("SELECT type FROM exercises WHERE circuit_id = :circuitId AND position = :position")
    ExerciseType getType(long circuitId, int position);

    @Update
    void updateExercises(List<Exercise> exercises);

    @Transaction
    default void updateDuration(long circuitId, int position, int duration) {
        ExerciseType type = getType(circuitId, position);
        Exercise exercise = Exercise.create(circuitId, position, duration, type);
        updateExercise(exercise);
    }

    @Transaction
    default void appendExercise(long circuitId, ExerciseType type) {
        int position = countExercises(circuitId);
        Exercise exercise = Exercise.create(circuitId, position, type);
        insertExercise(exercise);
    }

    @Transaction
    default void deleteExercises(long circuitId, List<Integer> positions) {
        List<Exercise> currentList = getExercisesNow(circuitId);
        List<Exercise> newList =
                currentList.stream().filter(exercise -> !positions.contains(exercise.getPosition()))
                        .collect(Collectors.toList());

        int size = newList.size();

        for (int i = 0; i < size; i++) {
            newList.set(i, newList.get(i).withPosition(i));
        }

        deleteExercises(currentList);
        insertExercises(newList);
    }

    @Transaction
    default void swapExercises(long circuitId, int fromPos, int toPos) {
        if (fromPos == toPos) {
            return;
        }

        int steps = Math.abs(toPos - fromPos);
        int direction = (int) Math.signum(toPos - fromPos);

        List<Exercise> oldExercises = getExercisesNow(circuitId);
        List<Exercise> updated = new ArrayList<>(steps);

        Exercise start = oldExercises.get(fromPos);

        for (int i = 0, j = fromPos; i < steps; i++, j += direction) {
            updated.add(oldExercises.get(j + direction).withPosition(j));
        }

        updated.add(start.withPosition(toPos));
        updateExercises(updated);
    }
}
