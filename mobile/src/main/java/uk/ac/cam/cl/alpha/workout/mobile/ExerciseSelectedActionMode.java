package uk.ac.cam.cl.alpha.workout.mobile;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.ActionMode;

import java.util.List;

import uk.ac.cam.cl.alpha.workout.R;
import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitEditModel;
import uk.ac.cam.cl.alpha.workout.shared.Exercise;

public class ExerciseSelectedActionMode implements ActionMode.Callback {

    private final EditActivity parentActivity;

    ExerciseSelectedActionMode(EditActivity theParentActivity) {
        parentActivity = theParentActivity;
    }

    // Called when action mode is started for the first time
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.exercise_selected_action_menu, menu);
        return true;
    }

    // Called each time the action mode is shown
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    // Called when user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_edited_circuit:
                mode.finish();
                return true;
            case R.id.delete_selected_exercises:
                deleteClickedHandler();
                return true;
            default:
                return false;
        }
    }

    // Called when user exits action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        parentActivity.finishExerciseActionMode();
    }

    private void deleteClickedHandler(){
        for (long key : parentActivity.getTracker().getSelection()) {
            List<Exercise> exercises = parentActivity.getModel().getExercises().getValue();
            if (exercises != null) {

                // TODO: Call delete exercise here
            }
        }
    }

}
