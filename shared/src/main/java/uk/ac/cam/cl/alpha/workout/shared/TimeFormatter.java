package uk.ac.cam.cl.alpha.workout.shared;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class TimeFormatter {
    private TimeFormatter(){}

    public static String format(int seconds) {
        SimpleDateFormat hoursMinutesSeconds = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat minutesSeconds = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String formatted;

        if (seconds > Constants.SECONDS_IN_HOUR) {
            formatted = hoursMinutesSeconds.format(new Date(seconds * Constants.MILLISECONDS_IN_SECOND));
        } else {
            formatted = minutesSeconds.format(new Date(seconds * Constants.MILLISECONDS_IN_SECOND));
        }

        return formatted;
    }
}
