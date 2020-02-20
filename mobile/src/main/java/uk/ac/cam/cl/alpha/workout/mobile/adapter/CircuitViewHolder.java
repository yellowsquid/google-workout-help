package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;

class CircuitViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTextView;
    private final TextView durationTextView;
    private final TextView restTextView;
    private final TextView lapsTextView;

    CircuitViewHolder(View view, OnItemClickListener itemClickListener) {
        super(view);

        view.setOnClickListener(v -> itemClickListener.onItemClick(getItemId(), v));
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
