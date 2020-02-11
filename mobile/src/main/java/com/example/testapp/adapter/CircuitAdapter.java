package com.example.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.shared.Circuit;

import java.util.ArrayList;
import java.util.List;

public class CircuitAdapter extends RecyclerView.Adapter {

    List<Circuit> circuits;

    public CircuitAdapter(List<? extends Circuit> theCircuits){
        circuits = new ArrayList<>();
        circuits.addAll(theCircuits);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.circuit_select_layout, parent, false);
        return new CircuitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Circuit circuit = circuits.get(position);
        CircuitViewHolder circuitViewHolder = (CircuitViewHolder) holder;

        circuitViewHolder.nameTextView.setText("Larry");
        circuitViewHolder.durationTextView.setText("Time: 10 mins");
        circuitViewHolder.restTextView.setText("Rest: 1 min");
        circuitViewHolder.lapsTextView.setText("Laps: " + circuit.getLaps());
    }

    @Override
    public int getItemCount() {
        return circuits.size();
    }

    public static class CircuitViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView durationTextView;
        public TextView restTextView;
        public TextView lapsTextView;

        public CircuitViewHolder(View view){
            super(view);

            nameTextView = view.findViewById(R.id.circuitName);
            durationTextView = view.findViewById(R.id.circuitDuration);
            restTextView = view.findViewById(R.id.circuitRestTime);
            lapsTextView = view.findViewById(R.id.circuitLaps);
        }
    }
}
