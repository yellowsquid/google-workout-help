package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.TimeFormatter;

public class CircuitViewHolder extends RecyclerView.ViewHolder {
    private final View baseView;
    private final TextView nameView;
    private final TextView durationView;
    private final TextView restView;
    private final TextView lapsView;

    CircuitViewHolder(View view, OnItemClickListener onClickListener) {
        super(view);

        baseView = view.findViewById(R.id.circuitCard);
        nameView = view.findViewById(R.id.circuitName);
        durationView = view.findViewById(R.id.circuitDuration);
        restView = view.findViewById(R.id.circuitRestTime);
        lapsView = view.findViewById(R.id.circuitLaps);

        baseView.setOnClickListener(v -> onClickListener.onItemClick(getItemId()));
    }

    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        return new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Override
            public Long getSelectionKey() {
                return getItemId();
            }
        };
    }

    public void setSelected(boolean selected) {
        DisplayMetrics metrics = baseView.getResources().getDisplayMetrics();
        float elevation = selected ? 8 : 2;
        baseView.setElevation(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevation, metrics));
    }

    void setCircuit(Circuit circuit) {
        Resources resources = nameView.getResources();
        int duration = circuit.sumExerciseDuration();
        int rest = circuit.sumRestDuration();
        int laps = circuit.getLaps();

        nameView.setText(circuit.getName());

        durationView
                .setText(resources.getString(R.string.duration_is, TimeFormatter.format(duration)));
        restView.setText(resources.getString(R.string.rest_is, TimeFormatter.format(rest)));
        lapsView.setText(resources.getString(R.string.laps_is, laps));
    }
}
