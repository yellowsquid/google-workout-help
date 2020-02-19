package uk.ac.cam.cl.alpha.workout.shared;

import androidx.room.ColumnInfo;
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

        return new AutoValue_BareCircuit(id, name, laps);
    }

    @AutoValue.CopyAnnotations
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public abstract long getId();

    @AutoValue.CopyAnnotations
    @ColumnInfo(name = "name")
    public abstract String getName();

    @AutoValue.CopyAnnotations
    @ColumnInfo(name = "laps")
    public abstract int getLaps();
}