package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.recyclerview.selection.MutableSelection;

@FunctionalInterface
public interface SelectionSetter {
    void copySelection(MutableSelection<Long> selection);
}
