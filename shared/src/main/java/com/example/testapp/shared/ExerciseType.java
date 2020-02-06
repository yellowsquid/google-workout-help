package com.example.testapp.shared;

/**
 * Enum for a type of exercise. This consists of a name and an icon.
 */
@SuppressWarnings("NonFinalStaticVariableUsedInClassInitialization")
public enum ExerciseType {
    BURPEES("burpees", R.drawable.workout_burpee), SITUPS("situps", R.drawable.workout_situp),
    RUSSIAN_TWISTS("russian twists", R.drawable.workout_russiantwist),
    PUSHUPS("pushups", R.drawable.workout_pushup),
    STAR_JUMPS("star jumps", R.drawable.workout_starjump),
    SQUATS("squats", R.drawable.workout_squat),
    REST("rest", R.drawable.workout_rest); // rest between / within laps is an exercise

    private final String name;
    private final int icon;

    ExerciseType(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}