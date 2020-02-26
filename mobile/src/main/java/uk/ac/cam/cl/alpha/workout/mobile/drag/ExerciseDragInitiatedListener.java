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

import java.util.ArrayList;

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

            View selectedView = null;
            ClipData.Item newItem;
            ArrayList<ClipData.Item> items = new ArrayList<>();
            for (Long key : snapshot) {
                selectedView = recyclerView.getChildAt(Math.toIntExact(key));
                newItem = new ClipData.Item((CharSequence) selectedView.getTag());
                items.add(newItem);
            }

            ClipData dragData = new ClipData("selected items", new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, items.get(0));

            for (int i = 1; i < items.size(); i++){
                dragData.addItem(items.get(i));
            }
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(selectedView);
            recyclerView.startDragAndDrop(dragData, shadow, null, 0);

            return true;
        } else {
            return false;
        }
    }
}
