package uk.ac.cam.cl.alpha.workout.shared;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import java.io.Serializable;
import java.util.Objects;

public class Exercise implements Serializable {
    private static final long serialVersionUID = -3063491187321789411L;
    private final ExerciseType exerciseType;
    private final int duration; // duration in seconds

    public Exercise(ExerciseType exerciseType, int duration) {
        Objects.requireNonNull(exerciseType);
        this.exerciseType = exerciseType;
        this.duration = duration;
    }

    @StringRes
    public int getName() {
        return exerciseType.getName();
    }

    @DrawableRes
    public int getIcon() {
        return exerciseType.getIcon();
    }

    public int getDuration() {
        return duration;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseType, duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Exercise exercise = (Exercise) obj;

        return duration == exercise.duration && exerciseType == exercise.exerciseType;
    }
}