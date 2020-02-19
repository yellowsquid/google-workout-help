package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class ExerciseTypeAdapter extends RecyclerView.Adapter<ExerciseTypeViewHolder> {
    @NonNull
    @Override
    public ExerciseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_exercise_layout, parent, false);
        return new ExerciseTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseTypeViewHolder holder, int position) {
        ExerciseType exerciseType = ExerciseType.values()[position];
        holder.setExerciseType(exerciseType);
    }

    @Override
    public int getItemCount() {
        return ExerciseType.values().length;
    }
}
