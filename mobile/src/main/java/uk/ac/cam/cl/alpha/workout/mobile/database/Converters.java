package uk.ac.cam.cl.alpha.workout.mobile.database;

import androidx.room.TypeConverter;

import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class Converters {
    @TypeConverter
    public static ExerciseType fromName(String name) {
        return name == null ? null : ExerciseType.valueOf(name);
    }

    @TypeConverter
    public static String exerciseTypeToString(ExerciseType type) {
        return type == null ? null : type.name();
    }
}
