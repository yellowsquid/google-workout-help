package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.View;

@FunctionalInterface
public interface OnItemClickListener {
    void onItemClick(long id, View holder);
}
