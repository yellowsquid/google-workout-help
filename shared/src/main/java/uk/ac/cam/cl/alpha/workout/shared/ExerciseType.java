package uk.ac.cam.cl.alpha.workout.shared;

/**
 * Enum for a type of exercise. This consists of a name and an icon.
 */
@SuppressWarnings("NonFinalStaticVariableUsedInClassInitialization")
public enum ExerciseType {
    BURPEES(R.string.burpees, R.drawable.workout_burpee),
    SITUPS(R.string.situps, R.drawable.workout_situp),
    RUSSIAN_TWISTS(R.string.russian_twists, R.drawable.workout_russiantwist),
    PUSHUPS(R.string.pushups, R.drawable.workout_pushup),
    STAR_JUMPS(R.string.star_jumps, R.drawable.workout_starjump),
    SQUATS(R.string.squats, R.drawable.workout_squat),
    REST(R.string.rest, R.drawable.workout_rest); // rest between / within laps is an exercise

    private final int name;
    private final int icon;

    ExerciseType(int name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}