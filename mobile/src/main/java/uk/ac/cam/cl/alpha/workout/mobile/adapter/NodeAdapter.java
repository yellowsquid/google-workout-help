package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.databinding.DeviceLayoutBinding;

public class NodeAdapter extends ListAdapter<String, DeviceViewHolder> {
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new StringItemCallback();

    public NodeAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DeviceLayoutBinding binding = DeviceLayoutBinding.inflate(inflater, parent, false);
        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.setText(getItem(position));
    }
}
