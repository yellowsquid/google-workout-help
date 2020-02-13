package uk.ac.cam.cl.alpha.workout.shared;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A list of exercises and a number of laps.
 */
public class Circuit implements Serializable {
    private static final long serialVersionUID = -7901856667294471903L;
    private final String name;
    private final List<? extends Exercise> exercises;
    private final int laps;

    public Circuit(String name, List<? extends Exercise> exercises, int laps) {
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("Circuit must contain at least one exercise.");
        }

        if (laps <= 0) {
            throw new IllegalArgumentException("Number of laps must be at least one.");
        }

        this.name = name;
        this.exercises = Collections.unmodifiableList(exercises);
        this.laps = laps;
    }

    public String getName() {
        return name;
    }

    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    public int getLaps() {
        return laps;
    }

    public int getExerciseCount() {
        return exercises.size();
    }

    public int getTotalDurationSecs() {
        int total = 0;
        for (Exercise exercise : exercises) {
            total += exercise.getDuration();
        }
        return total;
    };

    public int getTotalRestSecs() {
        int total = 0;
        for (Exercise exercise : exercises) {
            if(exercise.getExerciseType() == ExerciseType.REST) {
                total += exercise.getDuration();
            }
        }
        return total;
    };

    @Override
    public int hashCode() {
        return Objects.hash(exercises, laps);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Circuit circuit = (Circuit) obj;

        return laps == circuit.laps && exercises.equals(circuit.exercises);
    }

    public Exercise getExercise(int index) {
        return exercises.get(index);
    }
}