package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class CircuitKeyProvider extends ItemKeyProvider<Long> {
    private final RecyclerView recyclerView;

    public CircuitKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return recyclerView.getAdapter() == null ? null
                : recyclerView.getAdapter().getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForItemId(key);
        return holder == null ? RecyclerView.NO_POSITION : holder.getAdapterPosition();
    }
}
