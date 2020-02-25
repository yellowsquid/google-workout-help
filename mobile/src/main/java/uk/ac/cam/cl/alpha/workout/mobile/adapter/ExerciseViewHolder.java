package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

// Reference to the view of each data item.
public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTextView;
    private final EditText durationNumberPicker;
    private final ImageView exerciseImageView;

    ExerciseViewHolder(View view, DurationChangeListener listener) {
        super(view);
        view.setTag(Long.toString(getItemDetails().getSelectionKey()));

        nameTextView = view.findViewById(R.id.exercise_name);
        durationNumberPicker = view.findViewById(R.id.exercise_duration);
        exerciseImageView = view.findViewById(R.id.exerciseImageView);

        durationNumberPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int duration = Integer.parseInt(s.toString());

                    if (duration > 0) {
                        listener.onDurationChange(Math.toIntExact(getItemId()), duration);
                    }
                } catch (NumberFormatException e) {
                    Log.e("ExerciseViewHolder", "input not a number", e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        return new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return Math.toIntExact(getItemId());
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                return getItemId();
            }
        };
    }
}