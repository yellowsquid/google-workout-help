package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class AddExerciseAdapter extends RecyclerView.Adapter {
    private ExerciseType[] exerciseTypes;

    public AddExerciseAdapter(){
        exerciseTypes = ExerciseType.values();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_exercise_layout, parent, false);
        return new ExerciseNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExerciseType exerciseType = exerciseTypes[position];
        ExerciseNameViewHolder exerciseNameViewHolder = (ExerciseNameViewHolder) holder;
        exerciseNameViewHolder.exerciseNameTextView.setText(exerciseType.getName());
        // Use a tag to carry exercise type object back to circuit
        exerciseNameViewHolder.exerciseNameTextView.setTag(exerciseType);
    }

    @Override
    public int getItemCount() {
        return exerciseTypes.length;
    }

    static class ExerciseNameViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseNameTextView;

        public ExerciseNameViewHolder(View view){
            super(view);
            exerciseNameTextView = view.findViewById(R.id.exerciseNameTextView);
        }
    }
}
