package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseDragInitiatedListener implements OnDragInitiatedListener {
    private final RecyclerView recyclerView;
    private final SelectionSetter selectionSetter;

    public ExerciseDragInitiatedListener(RecyclerView recyclerView,
                                         SelectionSetter selectionSetter) {
        this.recyclerView = recyclerView;
        this.selectionSetter = selectionSetter;
    }

    @Override
    public boolean onDragInitiated(@NonNull MotionEvent e) {
        MutableSelection<Long> snapshot = new MutableSelection<>();
        selectionSetter.copySelection(snapshot);

        if (snapshot.isEmpty()) {
            ItemDetailsLookup.ItemDetails<Long> exerciseDetails =
                    new ExerciseDetailsLookup(recyclerView).getItemDetails(e);

            if (exerciseDetails != null) {
                snapshot.add(exerciseDetails.getSelectionKey());
            } else {
                return false;
            }
        }

        View selectedView = null;
        ClipData.Item newItem;
        ArrayList<ClipData.Item> items = new ArrayList<>();

        for (Long key : snapshot) {
            selectedView = recyclerView.findViewHolderForItemId(key).itemView;
            newItem = new ClipData.Item((CharSequence) selectedView.getTag());
            items.add(newItem);
        }

        // This is ugly because you can't make ClipData with multiple items at once.
        ClipData.Item first = items.remove(0);
        ClipData dragData =
                new ClipData("selected items", new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
                             first);

        for (ClipData.Item item : items) {
            dragData.addItem(item);
        }

        View.DragShadowBuilder shadow = new View.DragShadowBuilder(selectedView);
        recyclerView.startDragAndDrop(dragData, shadow, null, 0);

        return true;
    }
}
