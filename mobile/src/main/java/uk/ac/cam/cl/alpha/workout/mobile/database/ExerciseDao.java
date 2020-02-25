package uk.ac.cam.cl.alpha.workout.mobile.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uk.ac.cam.cl.alpha.workout.shared.Exercise;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

@Dao
interface ExerciseDao {
    @Insert
    void insertExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercises(List<Exercise> exercises);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId ORDER BY position")
    LiveData<List<Exercise>> getExercises(long circuitId);

    @Query("SELECT * FROM exercises WHERE circuit_id = :circuitId AND position = :position")
    LiveData<Exercise> getExercise(long circuitId, long position);

    @Query("SELECT count(*) FROM exercises WHERE circuit_id = :circuitId")
    int countExercises(long circuitId);

    @Query("SELECT type FROM exercises WHERE circuit_id = :circuitId AND position = :position")
    ExerciseType getType(long circuitId, int position);
}
