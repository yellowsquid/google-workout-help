package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.widget.TextView;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.databinding.ExerciseLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

// Reference to the view of each data item.
public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    private final ExerciseLayoutBinding binding;

    ExerciseViewHolder(ExerciseLayoutBinding binding, DurationChangeListener listener) {
        super(binding.getRoot());

        this.binding = binding;

        binding.duration.addTextChangedListener((NumberWatcher) number -> listener
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
        binding.getRoot().setActivated(selected);
    }

    void setExercise(Exercise exercise) {
        Resources resources = binding.getRoot().getResources();
        binding.name.setText(exercise.getName());
        binding.duration
                .setText(resources.getString(R.string.pure_duration, exercise.getDuration()),
                         TextView.BufferType.EDITABLE);
        binding.icon.setBackgroundResource(exercise.getIcon());
        binding.icon.setContentDescription(resources.getString(exercise.getName()));

        // Set the tag to be the item id so that it can be extracted for drag and drop
        itemView.setTag(Long.toString(getItemId()));
    }
}