package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.shared.Circuit;
import com.example.testapp.shared.Exercise;
import com.example.testapp.shared.ExerciseType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Exercise> exercises = new ArrayList<>(5);
        exercises.add(new Exercise(ExerciseType.BURPEES, 30));
        exercises.add(new Exercise(ExerciseType.PUSHUPS, 30));
        exercises.add(new Exercise(ExerciseType.RUSSIAN_TWISTS, 30));
        exercises.add(new Exercise(ExerciseType.SQUATS, 30));
        exercises.add(new Exercise(ExerciseType.STAR_JUMPS, 30));
        circuit = new Circuit(exercises, 5);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, WaitingActivity.class);
        intent.putExtra(WaitingActivity.CIRCUIT_ID, circuit);

        startActivity(intent);
    }
}
