package uk.ac.cam.cl.alpha.workout.mobile;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionTracker;

public class CircuitItemActivatedListener implements OnItemActivatedListener<Long> {

    private SelectionTracker<Long> tracker;

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }

    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item,
                                   @NonNull MotionEvent e) {
        if (tracker != null) {
            tracker.select(item.getSelectionKey());
        }
        return true;
    }
}
