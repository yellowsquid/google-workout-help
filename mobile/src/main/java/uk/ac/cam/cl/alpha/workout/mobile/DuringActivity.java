package uk.ac.cam.cl.alpha.workout.mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.databinding.ActivityDuringBinding;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.NodeAdapter;
import uk.ac.cam.cl.alpha.workout.mobile.model.ServerModel;

public class DuringActivity extends AppCompatActivity {
    public static final String CIRCUIT_ID = "uk.ac.cam.cl.alpha.workout.mobile.CIRCUIT_ID";
    private ServerModel serverModel;
    private boolean paused;
    private ActivityDuringBinding binding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        paused = false;
        // Retrieve circuit
        // TODO: something sensible on value 0
        long circuitId = getIntent().getLongExtra(CIRCUIT_ID, 0);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);


        NodeAdapter adaptor = new NodeAdapter(); // Make and implement adaptor

        serverModel = viewModelProvider.get(ServerModel.class);
        serverModel.setCircuitId(circuitId);
        serverModel.getDevicesList().observe(this, adaptor::submitList);

        serverModel.getCircuitName().observe(this, this::setTitle);

    }

    public void pauseClicked(View v) {
        if (paused) {
            binding.pauseButton.setText(R.string.pause);
            paused = false;
            serverModel.sendResumeSignal();
        } else {
            binding.pauseButton.setText(R.string.resume);
            paused = true;
            serverModel.sendPauseSignal();
        }

    }

    public void stopClicked(View v) {
        serverModel.sendStopSignal();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        serverModel.sendStopSignal();
        this.finish();
    }
}
