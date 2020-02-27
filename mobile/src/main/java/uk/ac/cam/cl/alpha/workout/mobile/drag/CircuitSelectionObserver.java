package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

import uk.ac.cam.cl.alpha.workout.mobile.MainActivity;

public class CircuitSelectionObserver extends SelectionTracker.SelectionObserver<Long> {
    private final MainActivity parentActivity;

    public CircuitSelectionObserver(MainActivity theParentActivity) {
        parentActivity = theParentActivity;
    }

    // Called when state of an item changes
    @Override
    public void onItemStateChanged(@NonNull Long key, boolean selected) {
        super.onItemStateChanged(key, selected);
        parentActivity.onCircuitStateChanged();
    }
}
