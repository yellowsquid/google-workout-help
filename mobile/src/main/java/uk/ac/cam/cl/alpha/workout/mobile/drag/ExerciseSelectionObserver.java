package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

import uk.ac.cam.cl.alpha.workout.mobile.EditActivity;

public class ExerciseSelectionObserver extends SelectionTracker.SelectionObserver<Long> {
    private final EditActivity parentActivity;

    public ExerciseSelectionObserver(EditActivity theParentActivity) {
        parentActivity = theParentActivity;
    }

    // Called when state of an item changes
    @Override
    public void onItemStateChanged(@NonNull Long key, boolean selected) {
        super.onItemStateChanged(key, selected);
        parentActivity.startExerciseActionMode();
    }
}
