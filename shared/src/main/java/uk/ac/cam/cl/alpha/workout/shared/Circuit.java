package uk.ac.cam.cl.alpha.workout.shared;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Circuit implements PureCircuit {
    private static final long serialVersionUID = 9190329129285998129L;
    @Embedded
    private final BareCircuit circuit;
    @Relation(parentColumn = "id", entityColumn = "circuit_id")
    private final List<Exercise> exercises;

    public Circuit(BareCircuit circuit, List<Exercise> exercises) {
        this.circuit = circuit;
        List<Exercise> list = new ArrayList<>(exercises);
        list.sort(null);
        this.exercises = Collections.unmodifiableList(list);
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
}
