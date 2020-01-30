package com.example.testapp.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A list of exercises and a number of laps.
 */
public class Circuit implements Serializable {
    private static final long serialVersionUID = 2L;

    // Fields should be final except serialisation is a b****

    private List<? extends Exercise> exercises;
    private int laps;

    public Circuit(List<? extends Exercise> exercises, int laps) {
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("Circuit must contain at least one exercise.");
        }

        if (laps <= 0) {
            throw new IllegalArgumentException("Number of laps must be at least one.");
        }

        this.exercises = Collections.unmodifiableList(exercises);
        this.laps = laps;
    }

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeInt(laps);
        out.writeObject(exercises);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        long receivedUid = in.readLong();
        if (receivedUid != serialVersionUID) {
            throw new ClassNotFoundException("wrong serial uid");
        }
        laps = in.readInt();
        exercises = Collections.unmodifiableList((List<? extends Exercise>) in.readObject());
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