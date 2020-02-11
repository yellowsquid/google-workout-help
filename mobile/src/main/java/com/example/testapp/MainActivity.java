package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.CircuitAdapter;
import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Exercise;
import com.example.testapp.shared.ExerciseType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String CIRCUIT_MESSAGE_PATH = "/circuit_path_name";
    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 20));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        circuit = new Circuit(exercises, 3);

        List<Exercise> exercises2 = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 20));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        Circuit circuit2 = new Circuit(exercises, 5);

        List<Circuit> testCircuits = new ArrayList<Circuit>();
        testCircuits.add(circuit);
        testCircuits.add(circuit2);

        RecyclerView circuitSelectRecyclerView = findViewById(R.id.circuitSelectRecyclerView);
        RecyclerView.LayoutManager circuitSelectLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter circuitSelectAdapter = new CircuitAdapter(testCircuits);

        circuitSelectRecyclerView.setLayoutManager(circuitSelectLayoutManager);
        circuitSelectRecyclerView.setAdapter(circuitSelectAdapter);

    }

    public void waitClicked(View v) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }

    public void editClicked(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.CIRCUIT_ID, circuit);
        startActivity(intent);
    }
}
