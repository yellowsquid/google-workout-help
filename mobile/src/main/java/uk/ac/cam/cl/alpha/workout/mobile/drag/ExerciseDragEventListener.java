package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.content.ClipData;
import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.mobile.model.CircuitEditModel;

public class ExerciseDragEventListener implements View.OnDragListener {
    private final CircuitEditModel model;

    public ExerciseDragEventListener(CircuitEditModel model) {
        this.model = model;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        int action = event.getAction();

        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            // Deliberate fall through, ignore these events
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_LOCATION:
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                return true;

            case DragEvent.ACTION_DROP:
                if (event.getClipData().getItemCount() != 1) {
                    return false;
                }

                RecyclerView recyclerView = (RecyclerView) v;

                ClipData.Item item = event.getClipData().getItemAt(0);
                View fromView = recyclerView.getChildAt(Integer.parseInt(item.getText().toString()));
                View toView = recyclerView.findChildViewUnder(event.getX(), event.getY());

                // For exercise list, id is position in lap.
                long fromPos = recyclerView.getChildItemId(fromView);
                long toPos = recyclerView.getChildItemId(toView);
                model.swapExercises(Math.toIntExact(fromPos), Math.toIntExact(toPos));
                return true;
            default:
                return false;

        }
    }
}
