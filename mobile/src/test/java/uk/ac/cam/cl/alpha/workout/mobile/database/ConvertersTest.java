package uk.ac.cam.cl.alpha.workout.mobile.database;

import org.junit.Test;

import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConvertersTest {

    @Test
    public void test_fromName_Null() {
        assertThat("null name is null exercise", Converters.fromName(null), nullValue());
    }
    //
    //    @Test
    //    public void test_fromName_NotNull() {
    //        assertThat("non-null name is non-null exercise", Converters.fromName(null),
    //        nullValue());
    //    }

    @Test
    public void test_toString_Null() {
        assertThat("null exercise is null string", Converters.exerciseTypeToString(null),
                   nullValue());
    }

    @Test
    public void test_toString_NotNull() {
        assertThat("non-null exercise is non-null string",
                   Converters.exerciseTypeToString(ExerciseType.BURPEES), is("BURPEES"));
    }
}