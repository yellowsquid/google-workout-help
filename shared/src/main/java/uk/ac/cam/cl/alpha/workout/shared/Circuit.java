package uk.ac.cam.cl.alpha.workout.shared;

import android.icu.text.Edits;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Circuit implements PureCircuit, Iterable<Exercise> {
    private static final long serialVersionUID = 9190329129285998129L;
    @Embedded
    private final BareCircuit circuit;
    @Relation(parentColumn = "id", entityColumn = "circuit_id")
    private final List<Exercise> exercises;

    public Circuit(BareCircuit circuit, List<Exercise> exercises) {
        this.circuit = circuit;
        this.exercises = Collections.unmodifiableList(exercises);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public long getId() {
        return circuit.getId();
    }

    @Override
    public String getName() {
        return circuit.getName();
    }

    @Override
    public int getLaps() {
        return circuit.getLaps();
    }

    public int countExercises() {
        return exercises.size();
    }

    public int sumExerciseDuration() {
        return exercises.stream().mapToInt(Exercise::getDuration).sum() * getLaps();
    }

    public int sumRestDuration() {
        return exercises.stream().filter(e -> e.getExerciseType() == ExerciseType.REST)
                .mapToInt(Exercise::getDuration).sum() * getLaps();
    }

    public Exercise getExercise(int index) {
        return exercises.get(index);
    }

    private class ExerciseIterator implements Iterator<Exercise> {
        private int exerciseIndex;
        private int lapIndex;

        ExerciseIterator() {
            exerciseIndex = 0;
            lapIndex = 0;
        }

        @Override
        public boolean hasNext() {
            // if not last lap or not last exercise
            return (lapIndex < getLaps() - 1 || exerciseIndex < countExercises() - 1);
        }

        @Override
        public Exercise next() {
            if(exerciseIndex < countExercises() - 1) {
                exerciseIndex++;
                return getExercise(exerciseIndex);
            } else if (exerciseIndex == countExercises() - 1 && lapIndex < getLaps() - 1) {
                exerciseIndex = 0;
                lapIndex++;
                return getExercise(exerciseIndex);
            } else {
                throw new NoSuchElementException("End of circuit.");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported by iterator.");
        }
    }

    @NonNull
    @Override
    public Iterator<Exercise> iterator() {
        return new ExerciseIterator();
    }
}
