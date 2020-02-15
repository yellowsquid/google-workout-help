package uk.ac.cam.cl.alpha.workout.mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.AddExerciseAdapter;

public class AddExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        RecyclerView addExerciseRecyclerView = findViewById(R.id.addExerciseRecyclerView);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        AddExerciseAdapter adapter = new AddExerciseAdapter();

        addExerciseRecyclerView.setLayoutManager(layout);
        addExerciseRecyclerView.setAdapter(adapter);

        addExerciseRecyclerView.addItemDecoration(new DividerItemDecoration(addExerciseRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

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
}
