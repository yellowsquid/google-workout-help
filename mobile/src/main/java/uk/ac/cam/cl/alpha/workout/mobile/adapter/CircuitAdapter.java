package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.PureCircuit;

public class CircuitAdapter extends ListAdapter<Circuit, CircuitAdapter.CircuitViewHolder> {
    private static final DiffUtil.ItemCallback<Circuit> DIFF_CALLBACK = new CircuitItemCallback<>();

    public CircuitAdapter() {
        super(DIFF_CALLBACK);
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
    }

    static class CircuitViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView durationTextView;
        private final TextView restTextView;
        private final TextView lapsTextView;

        CircuitViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.circuitName);
            durationTextView = view.findViewById(R.id.circuitDuration);
            restTextView = view.findViewById(R.id.circuitRestTime);
            lapsTextView = view.findViewById(R.id.circuitLaps);
        }

        void setCircuit(Circuit circuit) {
            Resources resources = nameTextView.getResources();
            int duration = circuit.sumExerciseDuration();
            int rest = circuit.sumRestDuration();
            int laps = circuit.getLaps();
            nameTextView.setText(circuit.getName());
            durationTextView.setText(resources.getString(R.string.duration_is, duration));
            restTextView.setText(resources.getString(R.string.rest_is, rest));
            lapsTextView.setText(resources.getString(R.string.laps_is, laps));
        }
    }

    static class CircuitItemCallback<T extends PureCircuit> extends DiffUtil.ItemCallback<T> {
        @Override
        public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return oldItem.equals(newItem);
        }
    }
}
