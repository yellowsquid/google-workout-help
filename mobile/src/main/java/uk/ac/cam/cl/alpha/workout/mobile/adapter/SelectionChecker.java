package uk.ac.cam.cl.alpha.workout.mobile.adapter;

@FunctionalInterface
public interface SelectionChecker {
    boolean isSelected(long id);
}
