package com.example.testapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter {
    private List<Exercise> exercises;

    public ExerciseAdapter(Circuit circuit) {
        exercises = circuit.getExercises();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_exercise_layout, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        ExerciseViewHolder exerciseViewHolder = (ExerciseViewHolder) holder;

        exerciseViewHolder.nameTextView.setText(exercise.getName());
        exerciseViewHolder.durationNumberPicker.setText(Integer.toString(exercise.getDuration()), TextView.BufferType.EDITABLE);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    // Reference to the view of each data item.
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public EditText durationNumberPicker;

        public ExerciseViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.exercise_name);
            durationNumberPicker = view.findViewById(R.id.exercise_duration);
        }
    }
}
