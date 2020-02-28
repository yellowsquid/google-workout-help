package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.databinding.CircuitLayoutBinding;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.TimeFormatter;

public class CircuitViewHolder extends RecyclerView.ViewHolder {
    private final CircuitLayoutBinding binding;

    CircuitViewHolder(CircuitLayoutBinding binding, OnItemClickListener onClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.card.setOnClickListener(v -> onClickListener.onItemClick(getItemId()));
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
        DisplayMetrics metrics = binding.card.getResources().getDisplayMetrics();
        float elevation = selected ? 8 : 2;
        binding.card.setElevation(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, elevation, metrics));
    }

    void setCircuit(Circuit circuit) {
        Resources resources = binding.getRoot().getResources();
        int duration = circuit.sumExerciseDuration();
        int rest = circuit.sumRestDuration();
        int laps = circuit.getLaps();

        binding.name.setText(circuit.getName());
        binding.duration
                .setText(resources.getString(R.string.duration_is, TimeFormatter.format(duration)));
        binding.rest.setText(resources.getString(R.string.rest_is, TimeFormatter.format(rest)));
        binding.laps.setText(resources.getString(R.string.laps_is, laps));
    }
}
