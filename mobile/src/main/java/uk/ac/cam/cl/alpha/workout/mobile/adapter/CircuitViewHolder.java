package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.shared.Circuit;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.TimeFormatter;

import static uk.ac.cam.cl.alpha.workout.shared.Constants.MILLISECONDS_IN_SECOND;
import static uk.ac.cam.cl.alpha.workout.shared.Constants.SECONDS_IN_HOUR;

class CircuitViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    private final TextView nameView;
    private final TextView durationView;
    private final TextView restView;
    private final TextView lapsView;

    CircuitViewHolder(View view, OnItemClickListener itemClickListener) {
        super(view);

        this.view = view;
        nameView = view.findViewById(R.id.circuitName);
        durationView = view.findViewById(R.id.circuitDuration);
        restView = view.findViewById(R.id.circuitRestTime);
        lapsView = view.findViewById(R.id.circuitLaps);

        View.OnClickListener listener = v -> itemClickListener.onItemClick(getItemId(), v);
        view.setOnClickListener(listener);
        view.getTouchables().forEach(v -> v.setOnClickListener(listener));
    }

    void setCircuit(Circuit circuit) {
        Resources resources = nameView.getResources();
        int duration = circuit.sumExerciseDuration();
        int rest = circuit.sumRestDuration();
        int laps = circuit.getLaps();

        nameView.setText(circuit.getName());

        durationView.setText(resources.getString(R.string.duration_is, TimeFormatter.format(duration)));
        restView.setText(resources.getString(R.string.rest_is, TimeFormatter.format(rest)));
        lapsView.setText(resources.getString(R.string.laps_is, laps));

        view.setBackgroundColor(resources.getColor(R.color.design_default_color_background));
    }
}
