package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

public class CircuitAdapter extends RecyclerView.Adapter<CircuitAdapter.CircuitViewHolder> {
    private List<Circuit> circuits;

    public CircuitAdapter(List<? extends Circuit> circuits) {
        this.circuits = new ArrayList<>();
        this.circuits.addAll(circuits);
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
        Circuit circuit = circuits.get(position);
        holder.setCircuit(circuit);
    }

    @Override
    public int getItemCount() {
        return circuits.size();
    }

    static class CircuitViewHolder extends RecyclerView.ViewHolder {
        // FIXME: encapsulation non-existent
        private TextView nameTextView;
        private TextView durationTextView;
        private TextView restTextView;
        private TextView lapsTextView;

        CircuitViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.circuitName);
            durationTextView = view.findViewById(R.id.circuitDuration);
            restTextView = view.findViewById(R.id.circuitRestTime);
            lapsTextView = view.findViewById(R.id.circuitLaps);
        }

        void setCircuit(Circuit circuit) {
            Resources resources = nameTextView.getResources();
            int duration = circuit.getTotalDurationSecs();
            int rest = circuit.getTotalRestSecs();
            int laps = circuit.getLaps();
            nameTextView.setText(circuit.getName());
            durationTextView.setText(resources.getString(R.string.duration_is, duration));
            restTextView.setText(resources.getString(R.string.rest_is, rest));
            lapsTextView.setText(resources.getString(R.string.laps_is, laps));
        }
    }
}
