package uk.ac.cam.cl.alpha.workout.shared;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class CircuitTest {
    private Circuit circuit;

    @Before
    public void setUp() {
        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(Exercise.create(0, 0, 30, ExerciseType.BURPEES));
        exercises.add(Exercise.create(0, 1, 30, ExerciseType.STAR_JUMPS));
        exercises.add(Exercise.create(0, 2, 30, ExerciseType.RUSSIAN_TWISTS));
        exercises.add(Exercise.create(0, 3, 30, ExerciseType.SITUPS));
        exercises.add(Exercise.create(0, 4, 15, ExerciseType.REST));
        BareCircuit pureCircuit = BareCircuit.create(0, "fred", 3);
        circuit = new Circuit(pureCircuit, exercises);
    }

    @Test
    public void testSerialize() throws Exception {
        byte[] buffer;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutput objectOut = new ObjectOutputStream(outputStream)) {
            objectOut.writeObject(circuit);
            objectOut.flush();
            buffer = outputStream.toByteArray();
        }

        Circuit deserialised;

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
             ObjectInputStream objectInput = new ObjectInputStream(inputStream)) {
            deserialised = (Circuit) objectInput.readObject();
        }

        assertThat("object survives serialisation", deserialised, CoreMatchers.equalTo(circuit));
    }
}