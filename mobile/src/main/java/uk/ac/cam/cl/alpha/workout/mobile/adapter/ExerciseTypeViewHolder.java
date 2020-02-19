package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

class ExerciseTypeViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;

    ExerciseTypeViewHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.exerciseNameTextView);
    }

    void setExerciseType(ExerciseType type) {
        textView.setText(type.getName());
        // Use a tag to carry exercise type object back to circuit
        textView.setTag(type);
    }
}
