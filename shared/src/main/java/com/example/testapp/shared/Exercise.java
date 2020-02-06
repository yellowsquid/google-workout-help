package com.example.testapp.shared;

import java.util.Objects;

public class Exercise {
    private final ExerciseType exerciseType;
    private final int duration; // duration in seconds

    public Exercise(ExerciseType exerciseType, int duration) {
        if (exerciseType == null) {
            throw new NullPointerException();
        }

        this.exerciseType = exerciseType;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Exercise exercise = (Exercise) o;

        return duration == exercise.duration &&
                exerciseType == exercise.exerciseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseType, duration);
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public int getTime() {
        return duration;
    }
}