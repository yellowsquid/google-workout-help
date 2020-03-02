package uk.ac.cam.cl.alpha.workout.shared;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeFormatter {

    public static final int TIMECONST = 60;

    private TimeFormatter(){}

    public static String format(int seconds) {
        String formatted;

        int sec = seconds % TIMECONST;
        int hour = seconds / TIMECONST;
        int min = hour % TIMECONST;
        hour /= TIMECONST;

        formatted = seconds >= Constants.SECONDS_IN_HOUR ? String.format("%d:%02d:%02d", hour, min, sec)
                : String.format("%02d:%02d", min, sec);

        return formatted;
    }
}
