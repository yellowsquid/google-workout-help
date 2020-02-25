package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new ExerciseDiffCallback();
    private final DurationChangeListener listener;
    private SelectionTracker tracker;

    public ExerciseAdapter(DurationChangeListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        setHasStableIds(true);
    }

    public void setTracker(SelectionTracker tracker) {
        this.tracker = tracker;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_exercise_layout, parent, false);
        return new ExerciseViewHolder(view, listener);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getPosition();
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.setExercise(getItem(position));

        if (tracker != null) {
            holder.itemView.setActivated(tracker.isSelected(getItemId(position)));
        }
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
