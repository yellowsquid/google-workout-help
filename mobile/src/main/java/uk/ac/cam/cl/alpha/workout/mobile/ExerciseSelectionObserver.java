package uk.ac.cam.cl.alpha.workout.mobile;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

public class ExerciseSelectionObserver extends SelectionTracker.SelectionObserver<Long> {

    private EditActivity parentActivity;

    ExerciseSelectionObserver (EditActivity theParentActivity){
        parentActivity = theParentActivity;
    }

    // Called when state of an item changes
    @Override
    public void onItemStateChanged(@NonNull Long key, boolean selected) {
        super.onItemStateChanged(key, selected);

        parentActivity.startExerciseActionMode();
    }

    // Called immediately after completion of any other (not refresh or restored) set of changes
    @Override
    public void onSelectionChanged() {
        super.onSelectionChanged();
    }

    // Called when underlying data set changes
    @Override
    public void onSelectionRefresh() {
        super.onSelectionRefresh();
    }

    // Called immediately after selection restored
    @Override
    public void onSelectionRestored() {
        super.onSelectionRestored();
    }
}
