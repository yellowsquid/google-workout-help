package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class CircuitAdapter extends ListAdapter<Circuit, CircuitViewHolder> {
    private static final DiffUtil.ItemCallback<Circuit> DIFF_CALLBACK = new CircuitItemCallback<>();
    private final SelectionChecker checker;

    public CircuitAdapter(SelectionChecker checker) {
        super(DIFF_CALLBACK);
        this.checker = checker;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CircuitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.circuit_select_layout, parent, false);
        return new CircuitViewHolder(view);
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
