package com.example.testapp.shared;

import java.util.Collections;
import java.util.List;

/**
 * A list of exercises and a number of laps.
 */
public class Circuit {
    private final List<Exercise> exercises;
    private final int laps;

    Circuit(List<Exercise> exercises, int laps) {
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("Circuit must contain at least one exercise.");
        }

        if (laps <= 0) {
            throw new IllegalArgumentException("Number of laps must be at least one.");
        }

        this.exercises = exercises;
        this.laps = laps;
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public int getLaps() {
        return laps;
    }
}