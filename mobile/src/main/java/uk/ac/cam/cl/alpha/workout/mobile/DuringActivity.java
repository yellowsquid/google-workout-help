package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NodeAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.SendTask;
import uk.ac.cam.cl.alpha.workout.mobile.model.ServerModel;
import uk.ac.cam.cl.alpha.workout.shared.Constants;
import uk.ac.cam.cl.alpha.workout.shared.Signal;

public class DuringActivity extends AppCompatActivity {


    // Override method in order to properly finish() this activity when the software back button is
    // pressed. This prevents a crash caused by onCreate() getting called again and trying to
    // populate things with a null circuit.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

// NEW

    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    private ServerModel serverModel;
    private boolean paused = false;
    private Button pauseButoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during);

        pauseButoon = findViewById(R.id.pauseButton);
        // Retrieve circuit
        // TODO: something sensible on value 0
        long circuitId = getIntent().getLongExtra(CIRCUIT_ID, 0);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);


        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        serverModel = viewModelProvider.get(ServerModel.class);
        serverModel.setCircuitId(circuitId);
        serverModel.getDevicesList().observe(this, adaptor::submitList);

    }

    public void pauseClicked(View v) {
        if (paused){
            pauseButoon.setText(R.string.pause);
            paused = false;
            serverModel.sendResumeSignal();
        } else {
            pauseButoon.setText(R.string.resume);
            paused = true;
            serverModel.sendPauseSignal();
        }

    }
}
