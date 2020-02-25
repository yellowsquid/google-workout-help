package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

@FunctionalInterface
public interface NumberWatcher extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    default void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            s.append('0');
        }

        try {
            int duration = Integer.parseInt(s.toString());

            if (duration > 0) {
                numberChanged(duration);
            }
        } catch (NumberFormatException e) {
            Log.e("NumberWatcher", "input not a number", e);
        }
    }

    void numberChanged(int number);
}
