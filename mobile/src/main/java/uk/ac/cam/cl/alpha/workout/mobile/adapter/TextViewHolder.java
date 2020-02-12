package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class TextViewHolder extends RecyclerView.ViewHolder {
    private final TextView view;

    TextViewHolder(TextView view) {
        super(view);
        this.view = view;
    }

    void setText(CharSequence text) {
        view.setText(text);
    }
}
