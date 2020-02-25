package uk.ac.cam.cl.alpha.workout.mobile;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ExerciseDragEventListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {

        final int action = event.getAction();

        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    return true;
                } else {
                    return false;
                }
            // Deliberate fall through, ignore these events
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_LOCATION:
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            case DragEvent.ACTION_DROP:
                RecyclerView parentRecycler = (RecyclerView) v;
                ClipData.Item item = event.getClipData().getItemAt(0);
                View fromView = parentRecycler.getChildAt(Integer.getInteger((String) item.getText()));
                View toView = parentRecycler.findChildViewUnder(event.getX(), event.getY());
                // TODO: Swap item positions in circuit

                return true;
            default:
                return false;

        }
    }
}
