package com.example.testapp.shared;

/**
 * Enum for a type of exercise. This consists of a name and an icon.
 */
public enum ExerciseType {
    BURPEES("burpees", "burpees.gif"),
    SITUPS("situps", "situps.gif"),
    RUSSIAN_TWISTS("russian twists", "russian-twists.gif"),
    PUSHUPS("pushups", "pushups.gif"),
    STAR_JUMPS("star jumps", "star-jumps.gif"),
    SQUATS("squats", "squats.gif"),
    REST("rest", "rest.gif"); // rest between / within laps is an exercise

    private final String name;
    private final String icon;

    ExerciseType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}