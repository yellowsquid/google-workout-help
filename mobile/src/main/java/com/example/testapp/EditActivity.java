package com.example.testapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapter.ExerciseAdapter;
import com.example.testapp.shared.Circuit;

public class EditActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "com.example.testapp.CIRCUIT_ID";

    private Circuit circuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve circuit and token.
        circuit = (Circuit) getIntent().getSerializableExtra(CIRCUIT_ID);

        RecyclerView recyclerView = findViewById(R.id.circuitEditRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new ExerciseAdapter(circuit);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
