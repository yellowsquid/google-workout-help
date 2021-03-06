package uk.ac.cam.cl.alpha.workout.shared;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

@AutoValue
@Entity(primaryKeys = {"circuit_id", "position"}, tableName = "exercises")
public abstract class Exercise implements Serializable {
    private static final long serialVersionUID = -3063491187321789411L;
    private static final int DEFAULT_DURATION = 30;

    @Ignore
    public static Exercise create(long circuitId, int position, ExerciseType exerciseType) {
        return create(circuitId, position, DEFAULT_DURATION, exerciseType);
    }

    public static Exercise create(long circuitId, int position, int duration,
                                  ExerciseType exerciseType) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        if (position < 0) {
            throw new IllegalArgumentException("Position must not be negative");
        }

        return new AutoValue_Exercise(circuitId, duration, exerciseType, position);
    }

    @AutoValue.CopyAnnotations
    @ColumnInfo(name = "circuit_id")
    @ForeignKey(entity = BareCircuit.class, parentColumns = "id", childColumns = "circuit_id",
                onDelete = ForeignKey.CASCADE)
    public abstract long getCircuitId();

    public abstract int getDuration();

    @AutoValue.CopyAnnotations
    @ColumnInfo(name = "type")
    @NonNull
    public abstract ExerciseType getExerciseType();

    @AutoValue.CopyAnnotations
    @ColumnInfo(name = "position")
    public abstract int getPosition();

    @StringRes
    public int getName() {
        return getExerciseType().getName();
    }

    @DrawableRes
    public int getIcon() {
        return getExerciseType().getIcon();
    }

    public Exercise withPosition(int position) {
        return create(getCircuitId(), position, getDuration(), getExerciseType());
    }
}