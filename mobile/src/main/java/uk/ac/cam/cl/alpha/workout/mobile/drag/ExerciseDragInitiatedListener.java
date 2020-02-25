package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseDragInitiatedListener implements OnDragInitiatedListener {

    private SelectionTracker<Long> registeredTracker;
    private RecyclerView recyclerView;

    public void setFields(SelectionTracker<Long> theTracker, RecyclerView theRecyclerView) {
        registeredTracker = theTracker;
        recyclerView = theRecyclerView;
    }

    @Override
    public boolean onDragInitiated(@NonNull MotionEvent e) {
        if (registeredTracker != null && recyclerView.getAdapter() != null) {

            MutableSelection<Long> snapshot = new MutableSelection<>();
            registeredTracker.copySelection(snapshot);

            if (snapshot.isEmpty()){
                ItemDetailsLookup.ItemDetails<Long>
                        exerciseDetails = new ExerciseDetailsLookup(recyclerView).getItemDetails(e);
                if (exerciseDetails != null) {
                    snapshot.add(exerciseDetails.getSelectionKey());
                }
                else {
                    return false;
                }
            }

            View v = recyclerView.getChildAt(Math.toIntExact(snapshot.iterator().next()));
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
            ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);

            recyclerView.startDragAndDrop(dragData, shadow, null, 0);

            return true;
        } else {
            return false;
        }
    }
}
