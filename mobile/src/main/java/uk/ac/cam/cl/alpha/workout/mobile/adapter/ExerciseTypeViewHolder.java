package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.databinding.ExerciseTypeLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

class ExerciseTypeViewHolder extends RecyclerView.ViewHolder {
    private final ExerciseTypeLayoutBinding binding;

    ExerciseTypeViewHolder(ExerciseTypeLayoutBinding binding, OnItemClickListener onClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.name.setOnClickListener(v -> onClickListener.onItemClick(getItemId()));
    }

    void setExerciseType(ExerciseType type) {
        binding.name.setText(type.getName());
        // Use a tag to carry exercise type object back to circuit
        binding.name.setTag(type);
    }
}
