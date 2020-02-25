package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

@FunctionalInterface
public interface NameWatcher extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    default void afterTextChanged(Editable s) {
        nameChanged(s.toString());
    }

    void nameChanged(String name);
}
