package uk.ac.cam.cl.alpha.workout.mobile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import uk.ac.cam.cl.alpha.workout.shared.BareCircuit;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

@Database(entities = {BareCircuit.class, Exercise.class}, exportSchema = false, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CircuitDao getCircuitDao();

    public abstract ExerciseDao getExerciseDao();
}
