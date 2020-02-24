package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new ExerciseDiffCallback();
    private SelectionTracker tracker;

    public ExerciseAdapter() {
        super(DIFF_CALLBACK);
        this.setHasStableIds(true);
    }

    public void setTracker(SelectionTracker tracker) {
        this.tracker = tracker;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_exercise_layout, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.setExercise(getItem(position));

        if (tracker != null) {
            holder.itemView.setActivated(tracker.isSelected(getItemId(position)));
        }
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    static class ExerciseDiffCallback extends DiffUtil.ItemCallback<Exercise> {
        @Override
        public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getCircuitId() == newItem.getCircuitId()
                    && oldItem.getPosition() == newItem.getPosition();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.equals(newItem);
        }
    }
}
