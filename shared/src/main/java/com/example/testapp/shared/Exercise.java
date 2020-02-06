package com.example.testapp.shared;

import java.util.Objects;

public class Exercise {
    private final ExerciseType exerciseType;
    private final int duration; // duration in seconds

    public Exercise(ExerciseType exerciseType, int duration) {
        Objects.requireNonNull(exerciseType);
        this.exerciseType = exerciseType;
        this.duration = duration;
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

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public String getName() {
        return exerciseType.getName();
    }

    public int getIcon() {
        return exerciseType.getIcon();
    }

    public int getDuration() {
        return duration;
    }
}