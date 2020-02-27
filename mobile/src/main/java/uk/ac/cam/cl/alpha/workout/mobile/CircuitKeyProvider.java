package uk.ac.cam.cl.alpha.workout.mobile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class CircuitKeyProvider extends ItemKeyProvider<Long> {
    private RecyclerView recyclerView;

    CircuitKeyProvider(RecyclerView theRecyclerView) {
        super(SCOPE_MAPPED);
        recyclerView = theRecyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        // Note that position seems to come in 1 too great, so we subtract 1 when we get item id.
        // Although strange, this subtraction seems to work.
        // This could be because of an unseen circuit in position 0 / something indexing from 1.
        return null != recyclerView.getAdapter() ? recyclerView.getAdapter().getItemId(position-1)
                : null;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForItemId(key);
        return null != holder ? holder.getAdapterPosition() : RecyclerView.NO_POSITION;
    }
}
