package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;

public class CircuitItemActivatedListener implements OnItemActivatedListener<Long> {
    private final ItemSelector selector;

    public CircuitItemActivatedListener(ItemSelector selector) {
        this.selector = selector;
    }

    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item,
                                   @NonNull MotionEvent e) {
        if (selector != null) {
            selector.select(item.getSelectionKey());
        }

        return true;
    }
}
