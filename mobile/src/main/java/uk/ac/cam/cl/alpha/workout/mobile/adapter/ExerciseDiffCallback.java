package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import uk.ac.cam.cl.alpha.workout.shared.Exercise;

class ExerciseDiffCallback extends DiffUtil.ItemCallback<Exercise> {
    @Override
    public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.getCircuitId() == newItem.getCircuitId() && oldItem.getPosition() == newItem
                .getPosition();
    }

    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.equals(newItem);
    }
}
