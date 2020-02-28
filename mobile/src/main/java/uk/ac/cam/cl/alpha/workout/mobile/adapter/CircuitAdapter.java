package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.databinding.CircuitLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class CircuitAdapter extends ListAdapter<Circuit, CircuitViewHolder> {
    private static final DiffUtil.ItemCallback<Circuit> DIFF_CALLBACK = new CircuitItemCallback<>();
    private final SelectionChecker checker;
    private final OnItemClickListener onClickListener;

    public CircuitAdapter(SelectionChecker checker, OnItemClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.checker = checker;
        this.onClickListener = onClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CircuitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CircuitLayoutBinding binding = CircuitLayoutBinding.inflate(inflater, parent, false);
        return new CircuitViewHolder(binding, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CircuitViewHolder holder, int position) {
        holder.setCircuit(getItem(position));
        holder.setSelected(checker.isSelected(getItemId(position)));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
