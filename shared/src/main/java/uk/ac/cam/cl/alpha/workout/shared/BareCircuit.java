package uk.ac.cam.cl.alpha.workout.shared;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

/**
 * A list of exercises and a number of laps.
 */
@AutoValue
@Entity(tableName = "circuits")
public abstract class BareCircuit implements PureCircuit {
    private static final long serialVersionUID = -7901856667294471903L;

    public static BareCircuit create(long id, String name, int laps) {
        if (laps <= 0) {
            throw new IllegalArgumentException("Number of laps must be positive.");
        }

        return new AutoValue_BareCircuit(name, laps, id);
    }

    @AutoValue.CopyAnnotations
    @PrimaryKey(autoGenerate = true)
    public abstract long getId();
}