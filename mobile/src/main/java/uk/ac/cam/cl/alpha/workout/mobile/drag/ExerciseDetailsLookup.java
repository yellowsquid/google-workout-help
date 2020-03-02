package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.mobile.adapter.ExerciseViewHolder;

public class ExerciseDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public ExerciseDetailsLookup(RecyclerView theRecyclerView) {
        recyclerView = theRecyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if (holder instanceof ExerciseViewHolder) {
                return ((ExerciseViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
