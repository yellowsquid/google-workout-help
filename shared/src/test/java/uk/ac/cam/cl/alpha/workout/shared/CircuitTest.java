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

import static org.junit.Assert.assertThat;

public class CircuitTest {
    private Circuit circuit;

    @Before
    public void setUp() {
        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 30));
        exercises.add(new Exercise(ExerciseType.SITUPS, 30));
        exercises.add(new Exercise(ExerciseType.REST, 15));
        circuit = new Circuit(exercises, 5);
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