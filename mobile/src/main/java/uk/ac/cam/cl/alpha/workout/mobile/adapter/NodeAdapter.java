package uk.ac.cam.cl.alpha.workout.mobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.cam.cl.alpha.workout.R;

public class NodeAdapter extends ListAdapter<String, TextViewHolder> {
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new StringItemCallback();

    public NodeAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView view = (TextView) inflater.inflate(R.layout.textbox, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.setText(getItem(position));
    }
}
