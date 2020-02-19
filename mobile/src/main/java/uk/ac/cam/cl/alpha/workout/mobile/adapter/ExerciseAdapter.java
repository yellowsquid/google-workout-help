package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new ExerciseDiffCallback();

    public ExerciseAdapter() {
        super(DIFF_CALLBACK);
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
    }

    // Reference to the view of each data item.
    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final EditText durationNumberPicker;
        private final ImageView exerciseImageView;


        ExerciseViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.exercise_name);
            durationNumberPicker = view.findViewById(R.id.exercise_duration);
            exerciseImageView = view.findViewById(R.id.exerciseImageView);
        }

        void setExercise(Exercise exercise) {
            Resources resources = nameTextView.getResources();
            int duration = exercise.getDuration();
            int name = exercise.getName();
            nameTextView.setText(name);
            durationNumberPicker.setText(resources.getString(R.string.pure_duration, duration),
                                         TextView.BufferType.EDITABLE);
            exerciseImageView.setBackgroundResource(exercise.getIcon());
            exerciseImageView.setContentDescription(resources.getString(name));
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
