package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

public class SelectionObserver extends SelectionTracker.SelectionObserver<Long> {
    private final OnStateChangedListener listener;

    public SelectionObserver(OnStateChangedListener listener) {
        this.listener = listener;
    }

    // Called when state of an item changes
    @Override
    public void onItemStateChanged(@NonNull Long key, boolean selected) {
        super.onItemStateChanged(key, selected);
        listener.onStateChanged();
    }
}
