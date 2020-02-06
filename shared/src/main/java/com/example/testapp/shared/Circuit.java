package com.example.testapp.shared;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A list of exercises and a number of laps.
 */
public class Circuit implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Exercise> exercises;
    private final int laps;

    public Circuit(List<Exercise> exercises, int laps) {
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("Circuit must contain at least one exercise.");
        }

        if (laps <= 0) {
            throw new IllegalArgumentException("Number of laps must be at least one.");
        }

        this.exercises = exercises;
        this.laps = laps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Circuit circuit = (Circuit) o;

        return laps == circuit.laps &&
                exercises.equals(circuit.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercises, laps);
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public int getLaps() {
        return laps;
    }

    public int getNumberOfExercises() {
        return exercises.size();
    }
}