package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.databinding.ExerciseLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseViewHolder> {
    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new ExerciseDiffCallback();
    private final DurationChangeListener listener;
    private final SelectionChecker checker;

    public ExerciseAdapter(DurationChangeListener listener, SelectionChecker checker) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.checker = checker;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ExerciseLayoutBinding binding = ExerciseLayoutBinding.inflate(inflater, parent, false);
        return new ExerciseViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.setExercise(getItem(position));
        holder.setSelected(checker.isSelected(getItemId(position)));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getPosition();
    }
}
