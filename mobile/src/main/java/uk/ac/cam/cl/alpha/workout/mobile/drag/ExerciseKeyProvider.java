package uk.ac.cam.cl.alpha.workout.mobile.drag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseKeyProvider extends ItemKeyProvider<Long> {

    private final RecyclerView recyclerView;

    public ExerciseKeyProvider(RecyclerView theRecyclerView) {
        super(SCOPE_MAPPED);
        recyclerView = theRecyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return null != recyclerView.getAdapter() ? recyclerView.getAdapter().getItemId(position)
                : null;
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForItemId(key);
        return null != holder ? holder.getAdapterPosition() : RecyclerView.NO_POSITION;
    }
}
