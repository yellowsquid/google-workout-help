package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.cam.cl.alpha.workout.databinding.DeviceLayoutBinding;

class DeviceViewHolder extends RecyclerView.ViewHolder {
    private final DeviceLayoutBinding binding;

    DeviceViewHolder(DeviceLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void setText(CharSequence text) {
        binding.name.setText(text);
    }
}
