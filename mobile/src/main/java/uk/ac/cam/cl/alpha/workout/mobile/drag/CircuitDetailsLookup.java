package uk.ac.cam.cl.alpha.workout.mobile.drag;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.mobile.adapter.CircuitViewHolder;

public class CircuitDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public CircuitDetailsLookup (RecyclerView theRecyclerView) {
        recyclerView = theRecyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if (holder instanceof CircuitViewHolder) {
                return ((CircuitViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
