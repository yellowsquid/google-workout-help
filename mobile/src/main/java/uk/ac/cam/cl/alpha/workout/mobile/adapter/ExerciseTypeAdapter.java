package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.databinding.ExerciseTypeLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class ExerciseTypeAdapter extends RecyclerView.Adapter<ExerciseTypeViewHolder> {
    private final OnItemClickListener onClickListener;

    public ExerciseTypeAdapter(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ExerciseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ExerciseTypeLayoutBinding binding =
                ExerciseTypeLayoutBinding.inflate(inflater, parent, false);
        return new ExerciseTypeViewHolder(binding, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseTypeViewHolder holder, int position) {
        ExerciseType exerciseType = ExerciseType.values()[position];
        holder.setExerciseType(exerciseType);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return ExerciseType.values().length;
    }
}
