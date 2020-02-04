package com.example.testapp.shared;

public class Exercise {
    private final ExerciseType exerciseType;
    private final int duration; // duration in seconds

    public Exercise(ExerciseType exerciseType, int duration) {
        this.exerciseType = exerciseType;
        this.duration = duration;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public int getTime() {
        return duration;
    }
}