package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

// Reference to the view of each data item.
public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTextView;
    private final EditText durationNumberPicker;
    private final ImageView exerciseImageView;
    private final View rootView;

    ExerciseViewHolder(View view, DurationChangeListener listener) {
        super(view);

        rootView = view;
        nameTextView = view.findViewById(R.id.exercise_name);
        durationNumberPicker = view.findViewById(R.id.exercise_duration);
        exerciseImageView = view.findViewById(R.id.exerciseImageView);

        durationNumberPicker.addTextChangedListener((NumberWatcher) number -> listener
                .onDurationChange(Math.toIntExact(getItemId()), number));
    }

    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        return new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Override
            public Long getSelectionKey() {
                return getItemId();
            }
        };
    }

    public void setSelected(boolean selected) {
        rootView.setActivated(selected);
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

        // Set the tag to be the item id so that it can be extracted for drag and drop
        itemView.setTag(Long.toString(getItemId()));
    }
}