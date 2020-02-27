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

    @Transaction
    default void updateExerciseDuration(long circuitId, int position, int duration) {
        ExerciseType type = getType(circuitId, position);
        Exercise exercise = Exercise.create(circuitId, position, duration, type);
        updateExercise(exercise);
    }

    @Delete
    void deleteExercises(List<Exercise> exercises);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId ORDER BY position")
    LiveData<List<Exercise>> getExercises(long circuitId);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId ORDER BY position")
    List<Exercise> getExercisesNow(long circuitId);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId AND position = :position")
    Exercise getExerciseNow(long circuitId, long position);

    @Query("SELECT count(*) FROM exercises WHERE circuit_id = :circuitId")
    int countExercises(long circuitId);

    @Query("SELECT type FROM exercises WHERE circuit_id = :circuitId AND position = :position")
    ExerciseType getType(long circuitId, int position);

    @Transaction
    default int appendExercise(long circuitId, ExerciseType type) {
        int position = countExercises(circuitId);
        Exercise exercise = Exercise.create(circuitId, position, type);
        insertExercise(exercise);
        return position;
    }

    @Transaction
    default void deleteExercises(long circuitId, List<Integer> positions) {
        List<Exercise> currentList = getExercisesNow(circuitId);
        List<Exercise> newList = new ArrayList<>(currentList.size());

        for (Exercise exercise : currentList) {
            if (!positions.contains(exercise.getPosition())) {
                newList.add(exercise);
            }
        }

        int size = newList.size();

        for (int i = 0; i < size; i++) {
            Exercise old = newList.get(i);
            newList.set(i, Exercise.create(circuitId, i, old.getDuration(), old.getExerciseType()));
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

        Exercise start = getExerciseNow(circuitId, fromPos);

        List<Exercise> updated = new ArrayList<>(steps);

        for (int i = 0, j = fromPos; i < steps; i++, j += direction) {
            Exercise next = getExerciseNow(circuitId, j + direction);
            updated.add(Exercise.create(circuitId, j, next.getDuration(), next.getExerciseType()));
        }

        updated.add(
                Exercise.create(circuitId, toPos, start.getDuration(), start.getExerciseType()));
        updateExercises(updated);
    }

    @Update
    void updateExercises(List<Exercise> exercises);
}
