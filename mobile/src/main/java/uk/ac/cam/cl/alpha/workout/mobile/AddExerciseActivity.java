package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.databinding.ActivityAddExerciseBinding;
import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseTypeAdapter;
import uk.ac.cam.cl.alpha.workout.shared.ExerciseType;

public class AddExerciseActivity extends AppCompatActivity {
    public static final String EXERCISE_ID = "uk.ac.cam.cl.alpha.workout.mobile.EXERCISE_ID";

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
        ActivityAddExerciseBinding binding =
                ActivityAddExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = binding.exerciseTypeList;
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        ExerciseTypeAdapter adapter = new ExerciseTypeAdapter(this::exerciseClicked);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                                                                 DividerItemDecoration.VERTICAL));
    }

    public void exerciseClicked(long id) {
        // Get exercise type out of the clicked view and pass it  back to the edit activity
        Intent result = new Intent();
        result.putExtra(EXERCISE_ID, ExerciseType.values()[Math.toIntExact(id)]);
        setResult(RESULT_OK, result);
        finish();
    }
}
